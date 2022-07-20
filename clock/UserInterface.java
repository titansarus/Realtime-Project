import javax.swing.*;
import javax.realtime.RealtimeThread;

interface IClock {
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

class UserInterface {
    public static void main(String args[]) throws InterruptedException {
        // extracted();
        IClock c = new DummyClock();
        // TerminalUI t = new TerminalUI(c);
        GUI t = new GUI(c);
        t.start();
        t.join();
    }

    private static void extracted() {
        JFrame frame = new JFrame("Clocks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        JButton button = new JButton("Press");
        frame.getContentPane().add(button); // Adds Button to content pane of frame
        frame.setVisible(true);
    }
}

class TerminalUI extends RealtimeThread {
    private IClock clock;

    public TerminalUI(IClock clock) {
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

class GUI extends RealtimeThread {
    private IClock clock;
    private JFrame frame;
    private JLabel timeLabel;

    public GUI(IClock clock) {
        this.clock = clock;
    }
    public void run() {
        this.frame = new JFrame("Clocks");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(300,300);
        this.timeLabel = new JLabel();
        this.timeLabel.setText("HE");
        JPanel p = new JPanel();
        p.add(this.timeLabel);
        this.frame.add(p);
        
        this.frame.setVisible(true);
        
        while (true) {
            int totalSeconds = this.clock.getTime();

            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = (totalSeconds % 3600) % 60;

            String t = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            int id = this.clock.getID();            
            this.showTime(t);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showTime(String t) {
        this.timeLabel.setText(t);
    }
}
