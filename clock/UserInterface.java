import javax.swing.*;
import javax.realtime.RealtimeThread;
import java.awt.*;

class ClockData {
    public int hours;
    public int minutes;
    public int seconds;

    ClockData(int totalSeconds) {
        this.hours = totalSeconds / 3600;
        this.minutes = (totalSeconds % 3600) / 60;
        this.seconds = (totalSeconds % 3600) % 60;
    }
}

interface IClock {
    int getID();
    int getTime();
}

class DummyClock implements IClock {
    public int getID() {
        return 1;
    }

    public int getTime() {
        return 1340;
    }
}

class UserInterface {
    
    public static void main(String args[]) throws InterruptedException {
        IClock c = new DummyClock();
        BaseFrame f = new BaseFrame();
        // TerminalUI t = new TerminalUI(c);
        GUI t = new GUI(c, f);

        t.start();
        t.join();
    }
}

class BaseFrame {
    private JFrame frame;
    private JPanel panel;

    public BaseFrame() {
        this.frame = new JFrame("Clocks");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(500, 500);

        this.panel = new JPanel();
        this.panel.setBackground(Color.BLACK);
        this.frame.add(panel);
        this.frame.setVisible(true);
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public void add(JComponent comp) {
        this.panel.add(comp);
    }
}


class TerminalUI extends RealtimeThread {
    private IClock clock;

    public TerminalUI(IClock clock) {
        this.clock = clock;
    }

    public void run() {
        while (true) {
            ClockData c = new ClockData(this.clock.getTime());
            int id = this.clock.getID();     
            String out = String.format("Clock %d: %02d:%02d:%02d", id, c.hours, c.minutes, c.seconds);
            System.out.println(out);
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
    private BaseFrame frame;
    private JLabel timeLabel;

    public GUI(IClock clock, BaseFrame frame) {
        this.clock = clock;
        this.frame = frame;
        this.InitLabel();
    }

    public void run() {
        while (true) {
            this.showTime();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void InitLabel(){
        this.timeLabel = new JLabel(String.format("Clock %d: 00:00:00",  this.clock.getID()));
        this.timeLabel.setFont(new Font("DIGITALDREAMFAT", Font.PLAIN, 30));
        this.timeLabel.setForeground(Color.RED);

        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        p.setBackground(Color.BLACK);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.setPreferredSize(new Dimension(500, 100));
        p.add(this.timeLabel);
        this.frame.add(p);
    }

    private void showTime() {
        ClockData c = new ClockData(this.clock.getTime());
        int id = this.clock.getID();     
        this.timeLabel.setText(String.format("Clock %d: %02d:%02d:%02d", id, c.hours, c.minutes, c.seconds));
    }
}
