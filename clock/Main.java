import java.lang.*;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.PeriodicParameters;

class PeriodicClockIncrementor extends RealtimeThread {
    PeriodicClockIncrementor(PeriodicParameters pp) {
        super(null, pp);
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            System.out.println("HELL");
            waitForNextPeriod();
        }
    }

}

public class Main {
    public static void main(String[] args) {

        int period = 500;

        PeriodicParameters pp = new PeriodicParameters(
            new RelativeTime(0, 0),    // Start
            new RelativeTime(period, 0), // Period
            new RelativeTime(period/2, 0),  // Cost
            new RelativeTime(period, 0), // Deadline
            null, // overrun handler
            null // miss handler
        );

        PeriodicClockIncrementor rtt = new PeriodicClockIncrementor(pp);
        rtt.start();
        try {
            rtt.join();
        } catch (InterruptedException ie) {
            // Ignore
        }
    }
}
