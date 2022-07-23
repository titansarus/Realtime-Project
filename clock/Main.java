import javax.realtime.RealtimeThread;
import java.util.ArrayList;

class Clock {
    private int time = 0;

    public int getTime() {
        return time;
    }

    public void increase() {
        this.time++;
    }

}


class ClockThread extends RealtimeThread implements IClock{
    private final Clock clock;
    private final int id;
    private final int innerOffset;
    private int innerTime;

    public ClockThread(Clock clock, int id, int innerOffset) {
        this.clock = clock;
        this.id = id;
        this.innerOffset = innerOffset;
    }

    public void run() {
        while (true) {
            this.innerTime = this.clock.getTime() + this.innerOffset;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTime() {
        return this.innerTime;
    }

    public int getID() {
        return this.id;
    }

}

class TimeStepThread extends RealtimeThread {
    private final Clock clock;

    public TimeStepThread(Clock clock) {
        this.clock = clock;
    }

    public void run() {
        while (true) {
            this.clock.increase();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Clock clock = new Clock();
        TimeStepThread timeStepThread = new TimeStepThread(clock);

        timeStepThread.setPriority(RealtimeThread.MAX_PRIORITY);
        
        ArrayList<ClockThread> printerThreads = new ArrayList<>();

        BaseFrame f = new BaseFrame();

        for (int i = 0; i < 4; i++) {
            ClockThread pt = new ClockThread(clock, i, 0);
            GUI gui = new GUI(pt, f);
            gui.start();
            gui.setPriority(RealtimeThread.MIN_PRIORITY);

            pt.setPriority(i + 1);
            printerThreads.add(pt);
        }

        printerThreads.get(3).setPriority(RealtimeThread.MAX_PRIORITY);
        timeStepThread.start();

        for (ClockThread pt : printerThreads) {
            pt.start();
        }

        for (ClockThread pt : printerThreads) {
            pt.join();
        }
        timeStepThread.join();

    }
}
