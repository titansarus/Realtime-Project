import javax.realtime.RealtimeThread;
import java.util.ArrayList;
import java.util.Collections;


public class Main {


    public static void main(String[] args) throws InterruptedException {
        Clock clock = new Clock();
        TimeStepThread timeStepThread = new TimeStepThread(clock);
        timeStepThread.setPriority(RealtimeThread.MAX_PRIORITY);
        ArrayList<ClockThread> printerThreads = new ArrayList<>();
        ArrayList<ThreadBlock> threadBlocks = new ArrayList<>();

        try {
            Setting.getInstance().initialize(args);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        BaseFrame f = null;
        if (Setting.getInstance().getUiType() == Setting.UiType.GUI) {
            f = new BaseFrame();
        }

        for (int i = 0; i < Setting.getInstance().getNumberOfThreads(); i++) {
            createClockThreadBlock(clock, printerThreads, threadBlocks,
                    Setting.getInstance().getPriorities().get(i), Setting.getInstance().getOffsets().get(i), i,
                    f
            );
        }

        Collections.sort(threadBlocks);
        CpuUtilityThread cpuUtilityThread = new CpuUtilityThread(threadBlocks);
        cpuUtilityThread.setPriority(RealtimeThread.MAX_PRIORITY);

        timeStepThread.start();
        cpuUtilityThread.start();
        for (ClockThread pt : printerThreads) {
            pt.start();
        }

        for (ClockThread pt : printerThreads) {
            pt.join();
        }
        timeStepThread.join();
        cpuUtilityThread.join();

    }

    private static void createClockThreadBlock(Clock clock, ArrayList<ClockThread> printerThreads, ArrayList<ThreadBlock> threadBlocks, int priority, int offset, int id, BaseFrame f) {
        ClockThread pt = new ClockThread(clock, id, offset);
        pt.setPriority(priority);

        UIThread ui;
        if (Setting.getInstance().getUiType() == Setting.UiType.GUI) {
            ui = new GUI(pt, f);
        } else {
            ui = new TerminalUI(pt);
        }

        ThreadBlock tb = new ThreadBlock(pt, ui);
        ui.start();
        ui.setPriority(RealtimeThread.MIN_PRIORITY);
        
        printerThreads.add(pt);
        threadBlocks.add(tb);
    }
}
