package multiscale.logic;

import javafx.scene.paint.Color;

public class Cell {
   private Color state;
   private int phase;

    public Cell(Color state, int phase) {
        this.state = state;
        this.phase = phase;
    }

    public Cell() {
        this.state = Color.WHITE;
        this.phase = 0;
    }

    public Color getState() {
        return state;
    }

    public void setState(Color state) {
        this.state = state;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
