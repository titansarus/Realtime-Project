import javax.realtime.RealtimeThread;

public class HelloRTWorld extends RealtimeThread {
    public void run() {
        System.out.println("Hello RT world!");
    }

    public static void main(String[] args) {
        HelloRTWorld rtt = new HelloRTWorld();
        rtt.start();
        try {
            rtt.join();
        } catch (InterruptedException ie) {
            // Ignore
        }
    }
}
