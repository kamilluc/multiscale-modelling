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
        cellsOld=new Cell[width][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){

                cells[i][j]=new Cell();
                cellsOld[i][j]=new Cell();
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
        if(cellsOld[x][y].getState().equals(Color.WHITE)){
            List<Color> closeColors=new ArrayList<>();

            if(!cellsOld[x+1][y].getState().equals(Color.WHITE) || !cellsOld[x+1][y].getState().equals(Color.BLACK) )
                closeColors.add(cellsOld[x+1][y].getState());

            if(!cellsOld[x-1][y].getState().equals(Color.WHITE) || !cellsOld[x-1][y].getState().equals(Color.BLACK) )
                closeColors.add(cellsOld[x-1][y].getState());

            if(!cellsOld[x][y+1].getState().equals(Color.WHITE) || !cellsOld[x][y+1].getState().equals(Color.BLACK))
                closeColors.add(cellsOld[x][y+1].getState());

            if(!cellsOld[x][y-1].getState().equals(Color.WHITE) || !cellsOld[x][y-1].getState().equals(Color.BLACK))
                closeColors.add(cellsOld[x][y-1].getState());

            if(!closeColors.isEmpty()){

                //TODO: add frequency
                Random rng=new Random();
                int rnd=rng.nextInt(closeColors.size());
                cells[x][y].setState(closeColors.get(rnd));
            }

//            Map<Color,Integer> map = new HashMap<>();
//            for(int i=0;i<closeColors.size();i++){
//                Integer count = map.get(closeColors.get(i));
//                map.put(closeColors.get(i), count==null?1:count+1);
//            }
//            System.out.println();
        }
    }

    private void seedGrains(int numberOfSeeds){
        Random rng = new Random();

        for(int i=0;i<numberOfSeeds;i++){
            int x=rng.nextInt((width-1))+1;
            int y=rng.nextInt((height-1))+1;
            Color newState;
//            do {
            //TODO: add checking for double seeds in same position
                 newState = Color.rgb(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256));
//            }
//            while (newState!=Color.WHITE && newState!=Color.BLACK);
            cells[x][y].setState(newState);

        }


        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
            }
        }

    }

    public boolean isBoardFull(){
        boolean status=true;
        for(int i=1;i<width-1;i++){
            for(int j=1;j<height-1;j++){
                if(cells[i][j].getState()==Color.WHITE) {
                    status = false;
                    break;
                }
                if(!status) break;
            }
        }
        return status;
    }

    public void nextSteep(){
        for(int i=1;i<width-1;i++){
            for(int j=1;j<height-1;j++){
                vonNeumann(i,j);
            }
        }

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
            }
        }
    }

}
