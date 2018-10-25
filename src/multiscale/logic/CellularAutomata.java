package multiscale.logic;

import javafx.scene.paint.Color;

import java.util.*;

public class CellularAutomata {
//    private Board board,boardOld;
private int width;
    private int height;
    public Cell[][] cells, cellsOld;

    public void initBoard() {


        cells=new Cell[width][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cells[i][j]=new Cell();
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
        seedGrains(4);

    }

    private void vonNeumann(int x, int y){
        if(cells[x][y].getState()== Color.WHITE){
            List<Color> closeColors=new ArrayList<>();

            if(cells[x+1][y].getState()!=Color.WHITE || cells[x+1][y].getState()!=Color.BLACK )
                closeColors.add(cells[x+1][y].getState());

            if(cells[x-1][y].getState()!=Color.WHITE || cells[x-1][y].getState()!=Color.BLACK )
                closeColors.add(cells[x-1][y].getState());

            if(cells[x][y+1].getState()!=Color.WHITE || cells[x][y+1].getState()!=Color.BLACK )
                closeColors.add(cells[x][y+1].getState());

            if(cells[x][y-1].getState()!=Color.WHITE || cells[x][y-1].getState()!=Color.BLACK )
                closeColors.add(cells[x][y-1].getState());

            Map<Color,Integer> map = new HashMap<>();
            for(int i=0;i<closeColors.size();i++){
                Integer count = map.get(closeColors.get(i));
                map.put(closeColors.get(i), count==null?1:count+1);
            }
            System.out.println();
        }
    }

    private void seedGrains(int numberOfSeeds){
        Random rng = new Random();

        for(int i=0;i<numberOfSeeds;i++){
            int x=rng.nextInt((width-1)-1);
            int y=rng.nextInt((height-1)-1);
            Color newState;
//            do {
                 newState = Color.rgb(rng.nextInt(255),rng.nextInt(255),rng.nextInt(255));
//            }
//            while (newState!=Color.WHITE && newState!=Color.BLACK);
            cells[x][y].setState(newState);

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
