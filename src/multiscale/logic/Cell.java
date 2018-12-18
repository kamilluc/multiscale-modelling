package multiscale.logic;

import javafx.scene.paint.Color;

public class Cell {
   private Color state;
   private int phase;
   private boolean isMCDone;
   private boolean recrystalized;
   private int h;

    public Cell(Color state, int phase) {
        this.state = state;
        this.phase = phase;
        this.isMCDone=false;
        h=5;
        recrystalized=false;

    }

    public Cell() {
        this.state = Color.WHITE;
        this.phase = 0;
        this.isMCDone=false;
        h=5;
        recrystalized=false;
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

    public boolean isMCDone() {
        return isMCDone;
    }

    public void setMCDone(boolean MCDone) {
        isMCDone = MCDone;
    }

    public boolean isRecrystalized() {
        return recrystalized;
    }

    public void setRecrystalized(boolean recrystalized) {
        this.recrystalized = recrystalized;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
}
