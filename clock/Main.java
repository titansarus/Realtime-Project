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
    private Clock clock;
    private int id;

    public PrinterThread(Clock clock, int id) {
        this.clock = clock;
        this.id = id;
    }

    public void run() {
        while (true) {
            System.out.println(id + " : " + this.clock.getTime());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class TimeStepThread extends RealtimeThread {
    private Clock clock;

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
        ArrayList<PrinterThread> printerThreads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            PrinterThread pt = new PrinterThread(clock,i);
            pt.setPriority(i+1);
            printerThreads.add(pt);
        }

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