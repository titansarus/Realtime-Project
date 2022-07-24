import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Setting {
    public final static int DEFAULT_CLOCK_COUNT = 4;
    public final static UiType DEFAULT_UI_TYPE = UiType.GUI;
    private static Setting instance = null;
    private UiType uiType;
    private int numberOfThreads;
    private List<Integer> priorities;
    private List<Integer> offsets;


    public static Setting getInstance() {
        if (instance == null) {
            instance = new Setting();
        }
        return instance;
    }

    private Setting() {
    }


    public enum UiType {
        GUI, TUI
    }

    public void initialize(String[] args) throws Exception {

        initializeUiType(args);
        initializeNumberOfThreads(args);
        initializePriorities(args);

        initializeOffsets(args);
    }

    private void initializeOffsets(String[] args) throws Exception {
        List<Integer> offsets = new ArrayList<>();
        if (args.length <= 2 + getNumberOfThreads()) {
            for (int i = 0; i < getNumberOfThreads(); i++) {
                offsets.add(0);
            }
        } else {
            if (args.length > 2 + getNumberOfThreads() && args.length < 2 + getNumberOfThreads() * 2) {
                throw new Exception("Invalid number of arguments");
            } else {
                for (int i = 2 + getNumberOfThreads(); i < 2 + 2 * getNumberOfThreads(); i++) {
                    offsets.add(Integer.parseInt(args[i]));
                }
            }
        }
        this.setOffsets(offsets);
    }

    private void initializePriorities(String[] args) throws Exception {
        List<Integer> priorities = new ArrayList<>();
        if (args.length <= 2) {
            for (int i = 0; i < getNumberOfThreads(); i++) {
                priorities.add(i + 1);
            }
        } else {
            if (args.length < 2 + getNumberOfThreads()) {
                throw new Exception("Invalid number of arguments");
            }
            for (int i = 2; i < 2 + getNumberOfThreads(); i++) {
                priorities.add(Integer.parseInt(args[i]));
            }
        }
        this.setPriorities(priorities);
    }

    private void initializeNumberOfThreads(String[] args) {
        int numberOfThreads;
        if (args.length < 2) {
            numberOfThreads = DEFAULT_CLOCK_COUNT;
        } else {
            numberOfThreads = Integer.parseInt(args[1]);
        }
        this.setNumberOfThreads(numberOfThreads);
    }

    private void initializeUiType(String[] args) {
        UiType uiType;
        if (args.length > 0) {
            if (Objects.equals(args[0], "0")) {
                uiType = UiType.GUI;
            } else {
                uiType = UiType.TUI;
            }
        } else {
            uiType = DEFAULT_UI_TYPE;
        }
        this.setUiType(uiType);
    }


    public UiType getUiType() {
        return uiType;
    }


    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public List<Integer> getPriorities() {
        return priorities;
    }

    public List<Integer> getOffsets() {
        return offsets;
    }

    public void setUiType(UiType uiType) {
        this.uiType = uiType;
    }


    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }


    public void setPriorities(List<Integer> priorities) {
        this.priorities = priorities;
    }

    public void setOffsets(List<Integer> offsets) {
        this.offsets = offsets;
    }
}
