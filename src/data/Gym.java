package data;

public class Gym {

    private int[] requiredHMs = new int[3];

    public Gym(int[] requiredHMs) {
        this.requiredHMs = requiredHMs;
    }

    public Gym() {
    }

    public int[] getRequiredHMs() {
        return requiredHMs;
    }

    public void setRequiredHMs(int[] requiredHMs) {
        this.requiredHMs = requiredHMs;
    }
}
