import javax.swing.*;
import javax.realtime.RealtimeThread;



interface IClock
{
    int getID();
    int getTime();
}


class DummyClock implements IClock {
    public int getID() {
        return 1;
    }
    public int getTime() {
        return 1452452;
    }
}


class gui{
    public static void main (String args[]) throws InterruptedException {
    //    extracted();
    IClock c = new DummyClock();
    TerminalClock t = new TerminalClock(c);
    t.start();
    t.join();
    }

    private static void extracted() {
        JFrame frame = new JFrame("Clocks");
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.setSize(300,300);
           JButton button = new JButton("Press");
           frame.getContentPane().add(button); // Adds Button to content pane of frame
           frame.setVisible(true);
    }
} 

class TerminalClock extends RealtimeThread {
    private IClock clock;

    public TerminalClock(IClock clock) {
        this.clock = clock;
    }
    public void run() {
        while (true) {
            int totalSeconds = this.clock.getTime();

            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = (totalSeconds % 3600) % 60;

            String t = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            
            int id = this.clock.getID();
            System.out.println(id + " : " + t);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
