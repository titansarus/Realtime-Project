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


class PrinterThread extends RealtimeThread {
    private final Clock clock;
    private final int id;
    private final int innerOffset;
    private int innerTime;

    public PrinterThread(Clock clock, int id, int innerOffset) {
        this.clock = clock;
        this.id = id;
        this.innerOffset = innerOffset;
    }

    public void run() {
        while (true) {
            this.innerTime = this.clock.getTime() + this.innerOffset;
            System.out.println(id + " : " + (this.innerTime));
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTime() {
        return this.innerTime;
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
                sleep(1);
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
        ArrayList<PrinterThread> printerThreads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            PrinterThread pt = new PrinterThread(clock, i, 0);
            pt.setPriority(i + 1);
            printerThreads.add(pt);
        }
        printerThreads.get(3).setPriority(RealtimeThread.MAX_PRIORITY);

        timeStepThread.start();


        for (PrinterThread pt : printerThreads) {
            pt.start();
        }

        for (PrinterThread pt : printerThreads) {
            pt.join();
        }
        timeStepThread.join();

    }
}
