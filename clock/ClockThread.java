import javolution.util.ReentrantLock;

import javax.realtime.RealtimeThread;


class Clock {
    private int time = 0;

    public int getTime() {
        return time;
    }

    public void increase() {
        this.time++;
    }

}

public class ClockThread extends RealtimeThread implements IClock {
    private final Clock clock;
    private final int id;
    private final int innerOffset;
    private int innerTime;
    private ReentrantLock lock;

    public ClockThread(Clock clock, int id, int innerOffset) {
        this.clock = clock;
        this.id = id;
        this.innerOffset = innerOffset;
        this.lock = new ReentrantLock();
    }

    public void run() {
        while (true) {
            this.lock.lock();
            this.innerTime = this.clock.getTime() + this.innerOffset;
            this.lock.unlock();
            try {
                sleep(107);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTime() {
        return this.innerTime;
    }

    public int getID() {
        return this.id;
    }

    public boolean setPriorityClock(int priority) {
        try {
            this.setPriority(priority);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }
}
