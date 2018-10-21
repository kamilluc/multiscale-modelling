package multiscale.logic;

import java.util.List;

public class Board {
    private int width;
    private int height;
    private Cell[][] cells;

    public Board(int width, int height) {
        this.width = width+2;
        this.height = height+2;

        cells=new Cell[width][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cells[i][j]=new Cell(0,0);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }
}
