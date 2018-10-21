package multiscale.logic;

public class Cell {
   private int state;
   private int phase;

    public Cell(int state, int phase) {
        this.state = state;
        this.phase = phase;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
