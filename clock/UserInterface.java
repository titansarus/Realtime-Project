import javax.swing.*;
import javax.realtime.RealtimeThread;

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
    private JFrame frame;
    private JLabel timeLabel;

    public GUI(IClock clock) {
        this.clock = clock;
    }

    public void run() {
        this.initFrame();
        while (true) {
            this.showTime();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initFrame(){
        this.frame = new JFrame("Clocks");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(300,300);
        this.timeLabel = new JLabel();
        this.timeLabel.setText("HE");
        JPanel p = new JPanel();
        p.add(this.timeLabel);
        this.frame.add(p);
        this.frame.setVisible(true);
    }

    private void showTime() {
        ClockData c = new ClockData(this.clock.getTime());
        int id = this.clock.getID();     
        this.timeLabel.setText(String.format("Clock %d: %02d:%02d:%02d", id, c.hours, c.minutes, c.seconds));
    }
}
