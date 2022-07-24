import javax.realtime.RealtimeThread;

public class TimeStepThread extends RealtimeThread {
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