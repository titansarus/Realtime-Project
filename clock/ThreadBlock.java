import javolution.util.ReentrantLock;



public class ThreadBlock implements Comparable<ThreadBlock> {
    private final ClockThread clockThread;
    private final UIThread uiThread;
    private final ReentrantLock lock;

    public ThreadBlock(ClockThread clockThread, UIThread uiThread) {
        this.clockThread = clockThread;
        this.uiThread = uiThread;
        this.lock = new ReentrantLock();
        this.getClockThread().setLock(lock);
    }

    @Override
    public int compareTo(ThreadBlock o) {
        return -(this.clockThread.getPriority() - o.clockThread.getPriority());
    }

    public ClockThread getClockThread() {
        return clockThread;
    }

    public UIThread getUiThread() {
        return uiThread;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}