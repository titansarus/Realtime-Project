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

    void setPriorityClock(int priority);
}

class DummyClock implements IClock {
    public int getID() {
        return 1;
    }

    public int getTime() {
        return 1340;
    }

    public void setPriorityClock(int priority) {
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
        this.frame.setSize(1000, 500);

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

class UIThread extends RealtimeThread {
}


class TerminalUI extends UIThread {
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
                sleep(107);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class GUI extends UIThread {
    private IClock clock;
    private BaseFrame frame;
    private JLabel timeLabel;
    private JButton button;
    private JTextField textField;

    public GUI(IClock clock, BaseFrame frame) {
        this.clock = clock;
        this.frame = frame;
        this.InitPanel();
    }

    public void run() {
        while (true) {
            this.showTime();
            try {
                sleep(107);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPriorityClock(int priority) {
        this.clock.setPriorityClock(priority);
    }

    private void InitPanel() {
        this.timeLabel = new JLabel(String.format("Clock %d: 00:00:00", this.clock.getID()));
        this.timeLabel.setFont(new Font("DIGITALDREAMFAT", Font.PLAIN, 30));
        this.timeLabel.setForeground(Color.RED);

        this.button = new JButton("Set!");
        this.button.setFont(new Font("DIGITALDREAMFAT", Font.PLAIN, 15));
        this.button.setForeground(Color.RED);
        this.button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.textField = new JTextField("input priority");
        this.textField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.textField.setFont(new Font("DIGITALDREAMFAT", Font.PLAIN, 15));
        this.textField.setMinimumSize(new java.awt.Dimension(100, 30));

        this.button.addActionListener(l -> {
            try {
                int priority = Integer.parseInt(this.textField.getText());
                this.setPriorityClock(priority);
                System.out.println("set priority to " + priority);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        });

        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        p.setBackground(Color.BLACK);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.setPreferredSize(new Dimension(500, 100));
        p.add(this.timeLabel);
        p.add(this.button);
        p.add(this.textField);
        this.frame.add(p);
    }

    private void showTime() {
        ClockData c = new ClockData(this.clock.getTime());
        int id = this.clock.getID();
        this.timeLabel.setText(String.format("Clock %d: %02d:%02d:%02d", id, c.hours, c.minutes, c.seconds));
    }
}
