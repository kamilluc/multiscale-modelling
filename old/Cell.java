import javafx.scene.paint.Color;

/**
 * Created by kamil on 03.05.2017.
 */
public class Cell {
    private Color color;
    private boolean state;
    private int id; //dodac i state 3 opcje
    private boolean radiusMark;
    private boolean recrystalized;
    private double ro;

    public Cell(){
        this.color = Color.BLACK;
        this.state = false;
        this.radiusMark=false;
        this.recrystalized=false;
this.ro=1;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRadiusMark() {
        return radiusMark;
    }

    public void setRadiusMark(boolean radiusMark) {
        this.radiusMark = radiusMark;
    }

    public boolean isRecrystalized() {
        return recrystalized;
    }

    public void setRecrystalized(boolean recrystalized) {
        this.recrystalized = recrystalized;
    }

    public double getRo() {
        return ro;
    }

    public void setRo(double ro) {
        this.ro = ro;
    }
}
