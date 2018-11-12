package multiscale.logic;

import javafx.scene.paint.Color;

import java.util.*;

public class CellularAutomata {
//    private Board board,boardOld;
private int width;
    private int height;
    public boolean extendedMoore;
    public int probablity4thRule;
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
        extendedMoore=false;
        probablity4thRule=10;
//        seedGrains(4);

    }

    private void vonNeumann(int x, int y){
        if(cellsOld[x][y].getState().equals(Color.WHITE)){
            List<Color> closeColors=new ArrayList<>();

            if(!cellsOld[x+1][y].getState().equals(Color.WHITE) && !cellsOld[x+1][y].getState().equals(Color.BLACK) && cellsOld[x+1][y].getPhase()!=2)
                closeColors.add(cellsOld[x+1][y].getState());

            if(!cellsOld[x-1][y].getState().equals(Color.WHITE) && !cellsOld[x-1][y].getState().equals(Color.BLACK) && cellsOld[x-1][y].getPhase()!=2 )
                closeColors.add(cellsOld[x-1][y].getState());

            if(!cellsOld[x][y+1].getState().equals(Color.WHITE) && !cellsOld[x][y+1].getState().equals(Color.BLACK) && cellsOld[x][y+1].getPhase()!=2)
                closeColors.add(cellsOld[x][y+1].getState());

            if(!cellsOld[x][y-1].getState().equals(Color.WHITE) && !cellsOld[x][y-1].getState().equals(Color.BLACK) && cellsOld[x][y-1].getPhase()!=2)
                closeColors.add(cellsOld[x][y-1].getState());

            if(!closeColors.isEmpty()){

                //TODO: add frequency


                //code from sandbox for frequency
//                List<Color> colors=new ArrayList<>();
//                colors.add(Color.RED);
//                colors.add(Color.RED);
//                colors.add(Color.WHITE);
//                colors.add(Color.BLUE);
//
                Map<Color, Integer> map=new HashMap<>();
//                //todo: remove list, use hashmap
                for(Color color: closeColors){
                    if(!map.containsKey(color))
                        map.put(color,1);
                    else
                        map.put(color, map.get(color)+1);
                }
//
//                System.out.println(Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());


                //end code
//
//
//                Random rng=new Random();
//                int rnd=rng.nextInt(closeColors.size());
//
//                cells[x][y].setState(closeColors.get(rnd));
                cells[x][y].setState(Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());

            }

//            Map<Color,Integer> map = new HashMap<>();
//            for(int i=0;i<closeColors.size();i++){
//                Integer count = map.get(closeColors.get(i));
//                map.put(closeColors.get(i), count==null?1:count+1);
//            }
//            System.out.println();
        }
    }

    public void seedGrains(int numberOfSeeds){
        Random rng = new Random();
//todo: check if randomized position is empty if not random once more
//        for(int i=0;i<numberOfSeeds;i++){
//            int x=rng.nextInt((width-1))+1;
//            int y=rng.nextInt((height-1))+1;
//            Color newState;
//
//            //TODO: add checking for double seeds in same position
//                 newState = Color.rgb(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256));
//
//            cells[x][y].setState(newState);
//
//        }

        int newSeeds=0;
        List<Color> colors=new ArrayList<>();

        while(newSeeds<numberOfSeeds) {
            int x=rng.nextInt((width-1))+1;
            int y=rng.nextInt((height-1))+1;
            Color newState= Color.rgb(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256));
            if(!colors.contains(newState) && !cells[x][y].getState().equals(Color.BLACK)){
                cells[x][y].setState(newState);
                colors.add(newState);
                newSeeds++;
            }
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
                if(cells[i][j].getState().equals(Color.WHITE)) {
                    status = false;
                }
                if(!status) break;
            }
            if(!status) break;
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

    public void addInclusions(String type, int number, double size){

        //System.out.println(type+" "+number+" "+size);
        List<Integer> axesX=new ArrayList<>();
        List<Integer> axesY=new ArrayList<>();

        if(type.contains("Random")) {
            do {
                Random rng = new Random();
                int x = rng.nextInt(width - 1) + 1;
                int y = rng.nextInt(height - 1) + 1;
                if (!cells[x][y].getState().equals(Color.BLACK)){
//                if (cells[x][y].getState().equals(Color.BLACK)){

                    axesX.add(x);
                    axesY.add(y);
                }
            }
            while (axesX.size() != number);
        }

        else {
            do {
                Random rng = new Random();
                int x = rng.nextInt(width - 1) + 1;
                int y = rng.nextInt(height - 1) + 1;
                boolean inBoundary=false;
                Color color=cells[x][y].getState();
                for(int i=-1;i<=1;i++) {
                    for (int j = -1; j <= 1; j++) {
                        if(x+i>0 && x+i<width-1 && y+j>0 && y+j<height-1) {
                            if (cells[x + i][y + j].getState() != color)
                                inBoundary = true;
                        }
                    }
                }
                if (!cells[x][y].getState().equals(Color.BLACK) && inBoundary) {
                    axesX.add(x);
                    axesY.add(y);
                }
            }
            while (axesX.size() != number);
        }


        if(type.contains("Circle")){
                           double r=size/2.0;

            for(int e=0;e<number;e++) {
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        if (((i - axesX.get(0)) * (i - axesX.get(0)) + (j - axesY.get(0)) * (j - axesY.get(0))) <= (r * r)){
                            if( i>0 && i<width-1 &&j >0 &&  j <height-1)
                                cells[i][j].setState(Color.BLACK);

                        }
                    }
                }
                axesX.remove(0);
                axesY.remove(0);
            }
        }
        else if(type.contains("Square")){
            int a=(int)((size/Math.sqrt(2.0))/2.0);
//            System.out.println(a);
            for(int e=0;e<number;e++) {
                for(int i=-a;i<=a;i++) {
                    for (int j = -a; j <= a; j++) {
                        if(axesX.get(0) + i>0 && axesX.get(0) + i<width-1 && axesY.get(0) + j >0 && axesY.get(0) + j <height-1)
                        cells[axesX.get(0) + i][axesY.get(0) + j].setState(Color.BLACK);
                    }
                }
                axesX.remove(0);
                axesY.remove(0);
            }
        }

//        for(int e=0;e<number;e++) {
            //todo: fix if inclusioon already exist in same place
//            if (type.equalsIgnoreCase("circle random")) {
//                double r=size/2.0;
//                Random rng=new Random();
//                int x=rng.nextInt(width-1)+1;
//                int y=rng.nextInt(height-1)+1;
//                for(int i=0;i<width;i++){
//                    for(int j=0;j<height;j++){
//                        if(((i-x)*(i-x)+(j-y)*(j-y))<=(r*r))
//                            cells[i][j].setState(Color.BLACK);
//                    }
//                }
//            } else if (type.equalsIgnoreCase("circle boundaries")) {
//
//            } else if (type.equalsIgnoreCase("square random")) {
//
//            } else if (type.equalsIgnoreCase("square boundaries")) {
//
//            }
//        }
//
//        for(int i=0;i<width;i++){
//            for(int j=0;j<height;j++){
//                cellsOld[i][j].setState(cells[i][j].getState());
//            }
//        }
        System.out.println("inclusions added");
    }


    public void removeNonSelectedGrains(List<Color> selectedGrains){
        for(int i=1;i<width-1;i++){
            for(int j=1;j<height-1;j++){
                if(!selectedGrains.contains(cellsOld[i][j].getState())){
                    if(!cellsOld[i][j].getState().equals(Color.BLACK)){
                        cells[i][j].setState(Color.WHITE);
                    }}
                    else
                        cells[i][j].setPhase(2);
            }
        }
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
                cellsOld[i][j].setPhase(cells[i][j].getPhase());
            }
        }
    }
}
