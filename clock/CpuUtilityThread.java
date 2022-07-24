import com.sun.management.OperatingSystemMXBean;

import javax.realtime.RealtimeThread;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;

public class CpuUtilityThread extends RealtimeThread {
    private final List<ThreadBlock> threadBlockList;

    public CpuUtilityThread(List<ThreadBlock> threadBlockList) {
        this.threadBlockList = threadBlockList;
    }

    public enum Percentage {
        HighPercentage(90, 101, 1, -1),
        MediumPercentage(70, 90, 2, 2),
        LowPercentage(0, 70, Integer.MAX_VALUE, Integer.MAX_VALUE);
        public final int lower_bound;
        public final int upper_bound;
        public final int lock_threshold;
        public final int unlock_threshold;

        Percentage(int lower_bound, int upper_bound, int lock_threshold, int unlock_threshold) {
            this.lower_bound = lower_bound;
            this.upper_bound = upper_bound;
            this.lock_threshold = lock_threshold;
            this.unlock_threshold = unlock_threshold;
        }

    }

    public void run() {
        while (true) {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            double cpu_load = osBean.getCpuLoad() * 100;
            System.out.println(cpu_load);
            for (Percentage p : Percentage.values()) {
                if (cpu_load > p.lower_bound && cpu_load <= p.upper_bound) {
                    handle(p);
                }
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handle(Percentage percentage) {
        Collections.sort(threadBlockList);
        for (int i = 0; i < min(threadBlockList.size(), percentage.unlock_threshold); i++) {
            try {
                threadBlockList.get(i).getLock().unlock();
            } catch (IllegalMonitorStateException ignored) {

            }
        }
        for (int i = min(percentage.lock_threshold, threadBlockList.size()); i < threadBlockList.size(); i++) {
            threadBlockList.get(i).getLock().lock();
        }
    }


}
