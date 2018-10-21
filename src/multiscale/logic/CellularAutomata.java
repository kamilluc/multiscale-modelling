package multiscale.logic;

import java.util.Random;

public class CellularAutomata {
//    private Board board,boardOld;
private int width;
    private int height;
    private Cell[][] cells, cellsOld;

    public void initBoard() {


        cells=new Cell[width][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cells[i][j]=new Cell(0,0);
            }
        }
    }


    public CellularAutomata(int width, int height){
//        board=new Board(width, height);
//
//  boardOld=board;

        this.width=width+2;
        this.height=height+2;
        initBoard();
        seedFirstGeneration();
    }

    private void vonNeumann(int x, int y){
        if(cells[x][y].getState()!=0){
            if(cells[x+1][y].getState()==0)
                cells[x+1][y].setState(cells[x][y].getState());
            if(cells[x-1][y].getState()==0)
                cells[x-1][y].setState(cells[x][y].getState());
            if(cells[x][y+1].getState()==0)
                cells[x][y+1].setState(cells[x][y].getState());
            if(cells[x][y-1].getState()==0)
                cells[x][y-1].setState(cells[x][y].getState());
        }
    }

    private void seedFirstGeneration(){
        int seeds=4;
        Random rng = new Random();

        for(int i=0;i<4;i++){
            int x=rng.nextInt((width-1)-1);
            int y=rng.nextInt((height-1)-1);
            int state=rng.nextInt(seeds+1);
            cells[x][y].setState(state);
        }
    }
    public void nextSteep(){
        for(int i=1;i<width-1;i++){
            for(int j=1;j<height-1;j++){
                vonNeumann(i,j);
            }
        }
    }
}
