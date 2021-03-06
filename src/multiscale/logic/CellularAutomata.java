package multiscale.logic;

import javafx.scene.paint.Color;
import java.util.*;

public class CellularAutomata {
    private int width;
    private int height;
    public boolean extendedMoore;
    public int probablity4thRule;
    public Cell[][] cells, cellsOld;
    private static int inclusions=0;
    List<Color> colorsMc;

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
        inclusions=0;
        this.width=width+2;
        this.height=height+2;
        initBoard();
        extendedMoore=false;
        probablity4thRule=10;
        colorsMc=new ArrayList<>();
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
                Map<Color, Integer> map=new HashMap<>();
                for(Color color: closeColors){
                    if(!map.containsKey(color))
                        map.put(color,1);
                    else
                        map.put(color, map.get(color)+1);
                }
                cells[x][y].setState(Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
            }
        }
    }

    private void extendedMoore(int x, int y){
        if(cellsOld[x][y].getState().equals(Color.WHITE)){
            boolean done=false;

            //rule 1
            List<Color> closeColors=new ArrayList<>();
            if(!cellsOld[x+1][y].getState().equals(Color.WHITE) && !cellsOld[x+1][y].getState().equals(Color.BLACK) && cellsOld[x+1][y].getPhase()!=2)
                closeColors.add(cellsOld[x+1][y].getState());

            if(!cellsOld[x-1][y].getState().equals(Color.WHITE) && !cellsOld[x-1][y].getState().equals(Color.BLACK) && cellsOld[x-1][y].getPhase()!=2 )
                closeColors.add(cellsOld[x-1][y].getState());

            if(!cellsOld[x][y+1].getState().equals(Color.WHITE) && !cellsOld[x][y+1].getState().equals(Color.BLACK) && cellsOld[x][y+1].getPhase()!=2)
                closeColors.add(cellsOld[x][y+1].getState());

            if(!cellsOld[x][y-1].getState().equals(Color.WHITE) && !cellsOld[x][y-1].getState().equals(Color.BLACK) && cellsOld[x][y-1].getPhase()!=2)
                closeColors.add(cellsOld[x][y-1].getState());

            if(!cellsOld[x+1][y+1].getState().equals(Color.WHITE) && !cellsOld[x+1][y+1].getState().equals(Color.BLACK) && cellsOld[x+1][y+1].getPhase()!=2)
                closeColors.add(cellsOld[x+1][y+1].getState());

            if(!cellsOld[x+1][y-1].getState().equals(Color.WHITE) && !cellsOld[x+1][y-1].getState().equals(Color.BLACK) && cellsOld[x+1][y-1].getPhase()!=2)
                closeColors.add(cellsOld[x+1][y-1].getState());

            if(!cellsOld[x-1][y-1].getState().equals(Color.WHITE) && !cellsOld[x-1][y-1].getState().equals(Color.BLACK) && cellsOld[x-1][y-1].getPhase()!=2)
                closeColors.add(cellsOld[x-1][y-1].getState());

            if(!cellsOld[x-1][y+1].getState().equals(Color.WHITE) && !cellsOld[x-1][y+1].getState().equals(Color.BLACK) && cellsOld[x-1][y+1].getPhase()!=2)
                closeColors.add(cellsOld[x-1][y+1].getState());

            if(!closeColors.isEmpty()){
                Map<Color, Integer> map=new HashMap<>();
                for(Color color: closeColors){
                    if(!map.containsKey(color))
                        map.put(color,1);
                    else
                        map.put(color, map.get(color)+1);
                }
                if(Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue()>=5){
                    done=true;
                    cells[x][y].setState(Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                }
            }


            //rule 2
            if(!done){
                closeColors.clear();
                if(!cellsOld[x+1][y].getState().equals(Color.WHITE) && !cellsOld[x+1][y].getState().equals(Color.BLACK) && cellsOld[x+1][y].getPhase()!=2)
                    closeColors.add(cellsOld[x+1][y].getState());

                if(!cellsOld[x-1][y].getState().equals(Color.WHITE) && !cellsOld[x-1][y].getState().equals(Color.BLACK) && cellsOld[x-1][y].getPhase()!=2 )
                    closeColors.add(cellsOld[x-1][y].getState());

                if(!cellsOld[x][y+1].getState().equals(Color.WHITE) && !cellsOld[x][y+1].getState().equals(Color.BLACK) && cellsOld[x][y+1].getPhase()!=2)
                    closeColors.add(cellsOld[x][y+1].getState());

                if(!cellsOld[x][y-1].getState().equals(Color.WHITE) && !cellsOld[x][y-1].getState().equals(Color.BLACK) && cellsOld[x][y-1].getPhase()!=2)
                    closeColors.add(cellsOld[x][y-1].getState());

                    Map<Color, Integer> map=new HashMap<>();
                    for(Color color: closeColors){
                        if(!map.containsKey(color))
                            map.put(color,1);
                        else
                            map.put(color, map.get(color)+1);
                    }

                if(!map.isEmpty() && Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue()>=3){
                    done=true;
                    cells[x][y].setState(Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                }
            }


            //rule 3
            if(!done){
                closeColors.clear();
                if(!cellsOld[x+1][y+1].getState().equals(Color.WHITE) && !cellsOld[x+1][y+1].getState().equals(Color.BLACK) && cellsOld[x+1][y+1].getPhase()!=2)
                    closeColors.add(cellsOld[x+1][y+1].getState());


                if(!cellsOld[x+1][y-1].getState().equals(Color.WHITE) && !cellsOld[x+1][y-1].getState().equals(Color.BLACK) && cellsOld[x+1][y-1].getPhase()!=2)
                    closeColors.add(cellsOld[x+1][y-1].getState());

                if(!cellsOld[x-1][y-1].getState().equals(Color.WHITE) && !cellsOld[x-1][y-1].getState().equals(Color.BLACK) && cellsOld[x-1][y-1].getPhase()!=2)
                    closeColors.add(cellsOld[x-1][y-1].getState());

                if(!cellsOld[x-1][y+1].getState().equals(Color.WHITE) && !cellsOld[x-1][y+1].getState().equals(Color.BLACK) && cellsOld[x-1][y+1].getPhase()!=2)
                    closeColors.add(cellsOld[x-1][y+1].getState());

                Map<Color, Integer> map=new HashMap<>();
                for(Color color: closeColors){
                    if(!map.containsKey(color))
                        map.put(color,1);
                    else
                        map.put(color, map.get(color)+1);
                }
                if(!map.isEmpty() && Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue()>=3){
                    done=true;
                    cells[x][y].setState(Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                }
            }

            //rule 4

            if(!done){
                closeColors.clear();
                Random rng=new Random();
                if(rng.nextInt(100)+1<probablity4thRule){
                    if(!cellsOld[x+1][y].getState().equals(Color.WHITE) && !cellsOld[x+1][y].getState().equals(Color.BLACK) && cellsOld[x+1][y].getPhase()!=2)
                        closeColors.add(cellsOld[x+1][y].getState());

                    if(!cellsOld[x-1][y].getState().equals(Color.WHITE) && !cellsOld[x-1][y].getState().equals(Color.BLACK) && cellsOld[x-1][y].getPhase()!=2 )
                        closeColors.add(cellsOld[x-1][y].getState());

                    if(!cellsOld[x][y+1].getState().equals(Color.WHITE) && !cellsOld[x][y+1].getState().equals(Color.BLACK) && cellsOld[x][y+1].getPhase()!=2)
                        closeColors.add(cellsOld[x][y+1].getState());

                    if(!cellsOld[x][y-1].getState().equals(Color.WHITE) && !cellsOld[x][y-1].getState().equals(Color.BLACK) && cellsOld[x][y-1].getPhase()!=2)
                        closeColors.add(cellsOld[x][y-1].getState());

                    if(!cellsOld[x+1][y+1].getState().equals(Color.WHITE) && !cellsOld[x+1][y+1].getState().equals(Color.BLACK) && cellsOld[x+1][y+1].getPhase()!=2)
                        closeColors.add(cellsOld[x+1][y+1].getState());

                    if(!cellsOld[x+1][y-1].getState().equals(Color.WHITE) && !cellsOld[x+1][y-1].getState().equals(Color.BLACK) && cellsOld[x+1][y-1].getPhase()!=2)
                        closeColors.add(cellsOld[x+1][y-1].getState());

                    if(!cellsOld[x-1][y-1].getState().equals(Color.WHITE) && !cellsOld[x-1][y-1].getState().equals(Color.BLACK) && cellsOld[x-1][y-1].getPhase()!=2)
                        closeColors.add(cellsOld[x-1][y-1].getState());

                    if(!cellsOld[x-1][y+1].getState().equals(Color.WHITE) && !cellsOld[x-1][y+1].getState().equals(Color.BLACK) && cellsOld[x-1][y+1].getPhase()!=2)
                        closeColors.add(cellsOld[x-1][y+1].getState());

                    if(!closeColors.isEmpty()){
                        Map<Color, Integer> map=new HashMap<>();
                        for(Color color: closeColors){
                            if(!map.containsKey(color))
                                map.put(color,1);
                            else
                                map.put(color, map.get(color)+1);
                        }
                        cells[x][y].setState(Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                            done=true;
                        }

                    }
                }
            }
    }

    public void seedGrains(int numberOfSeeds){
        Random rng = new Random();
        int newSeeds=0;
        List<Color> colors=new ArrayList<>();

        while(newSeeds<numberOfSeeds) {
            int x=rng.nextInt((width-1))+1;
            int y=rng.nextInt((height-1))+1;
            Color newState= Color.rgb(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256));
            if(!colors.contains(newState) && cellsOld[x][y].getState().equals(Color.WHITE)){
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
            //    if(cells[i][j].getPhase()!=888) {
                    if (extendedMoore) extendedMoore(i, j);
                    else vonNeumann(i, j);
              //  }
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
        int size2=(int)(size/2.0);
        if(type.contains("Random")) {
            do {
                Random rng = new Random();
//                int x = rng.nextInt(width - 1+size2) + 1-size2;
//                int y = rng.nextInt(height - 1+size2) + 1-size2;
                int x = rng.nextInt((width-size2*2))+size2;
                int y = rng.nextInt((height-size2*2))+size2;
                if (!cells[x][y].getState().equals(Color.BLACK)){
//                if (cells[x][y].getState().equals(Color.BLACK)){


//                    for(int w=0;w<axesX.size();w++){
//                        int xx=axesX.get(w);
//                        int yy=axesY.get(w);
//                        if(x)
//                    }


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
                        if(axesX.get(0) + i>0 && axesX.get(0) + i<width-1 && axesY.get(0) + j >0 && axesY.get(0) + j <height-1) {
                            if (i > 0 && i < width - 1 && j > 0 && j < height - 1)

                                cells[axesX.get(0) + i][axesY.get(0) + j].setState(Color.BLACK);
                        }
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
//        System.out.println("inclusions added");
    }

    public void addInclusionsV2(String type, int number, double size) {
        Random rng=new Random();
        int currentNumber=0;
        boolean checker=false;
        boolean inBoundary=false;

        if(type.contains("Square")) {
            int a = (int) ((size / Math.sqrt(2.0)) / 2.0);
            while (currentNumber < number) {
                checker = false;
                inBoundary=false;
                int x = rng.nextInt((width - a * 2-1)) + 1;
                int y = rng.nextInt((height - a * 2-1)) + 1;
                if (!cells[x][y].getState().equals(Color.BLACK)) {
                    if(type.contains("Random")) {
                        if (cells[x + 2*a][y].getState().equals(Color.BLACK) || cells[x + 2*a][y + 2*a].getState().equals(Color.BLACK) || cells[x][y + 2*a].getState().equals(Color.BLACK))
                            checker = true;
                        if (!checker) {
                            for(int i=x;i<=x+2*a;i++) {
                                for (int j = y; j <= y + 2 * a; j++) {
                                    cells[i][j].setState(Color.BLACK);
                                }
                            }
                            currentNumber++;
                            inclusions++;
                        }
                    }
                    else{
                        Color color=cells[x][y].getState();
                        for(int i=-1;i<=1;i++) {
                            for (int j = -1; j <= 1; j++) {
                                if(x+i>0 && x+i<width-1 && y+j>0 && y+j<height-1) {
                                    if (cells[x + i][y + j].getState() != color){
                                        inBoundary = true;
                                    break;}
                                }
                            }
                        }

                        if (inBoundary) {
                            if (cells[x- a][y+a].getState().equals(Color.BLACK) || cells[x + a][y - a].getState().equals(Color.BLACK) || cells[x+a][y + a].getState().equals(Color.BLACK) || cells[x-a][y -a].getState().equals(Color.BLACK))
                                checker = true;
                            if (!checker) {
                                for(int i=x-a;i<=x+a;i++) {
                                    for (int j = y-a; j <= y + a; j++) {
                                        cells[i][j].setState(Color.BLACK);
                                    }
                                }
                                currentNumber++;
                                inclusions++;
                            }
                        }
                    }
                }
            }
        }


        if(type.contains("Circle")) {
            int r = (int) (size / 2.0);
            while (currentNumber < number) {
                checker = false;
                inBoundary=false;
                int x = rng.nextInt((width - r * 2-1)) + r;
                int y = rng.nextInt((height - r * 2-1)) +r;
                if (!cells[x][y].getState().equals(Color.BLACK)) {
                    if(type.contains("Random")) {
                        if (cells[x][y+r].getState().equals(Color.BLACK) || cells[x][y-r].getState().equals(Color.BLACK) || cells[x-r][y].getState().equals(Color.BLACK) || cells[x+r][y].getState().equals(Color.BLACK) || cells[x+r][y+r].getState().equals(Color.BLACK) || cells[x+r][y-r].getState().equals(Color.BLACK) || cells[x-r][y + r].getState().equals(Color.BLACK) || cells[x-r][y - r].getState().equals(Color.BLACK))
                            checker = true;
                        if (!checker) {
                            for(int i=x-r;i<=x+r;i++) {
                                for (int j = y-r; j <= y + r; j++) {
                                    if (((i - x) * (i - x) + (j - y) * (j - y)) <= (r * r)){
                                        if( i>0 && i<width-1 &&j >0 &&  j <height-1)
                                            cells[i][j].setState(Color.BLACK);

                                    }
                                }
                            }
                            currentNumber++;
                            inclusions++;
                        }
                    }
                    else{
                        Color color=cells[x][y].getState();
                        for(int i=-1;i<=1;i++) {
                            for (int j = -1; j <= 1; j++) {
                                if(x+i>0 && x+i<width-1 && y+j>0 && y+j<height-1) {
                                    if (cells[x + i][y + j].getState() != color){
                                        inBoundary = true;
                                        break;}
                                }
                            }
                        }

                        if (inBoundary) {
                            if (cells[x][y+r].getState().equals(Color.BLACK) || cells[x][y-r].getState().equals(Color.BLACK) || cells[x-r][y].getState().equals(Color.BLACK) || cells[x+r][y].getState().equals(Color.BLACK) || cells[x+r][y+r].getState().equals(Color.BLACK) || cells[x+r][y-r].getState().equals(Color.BLACK) || cells[x-r][y + r].getState().equals(Color.BLACK) || cells[x-r][y - r].getState().equals(Color.BLACK))
                                checker = true;
                            if (!checker) {
                                for(int i=x-r;i<=x+r;i++) {
                                    for (int j = y-r; j <= y + r; j++) {
                                        if (((i - x) * (i - x) + (j - y) * (j - y)) <= (r * r)){
                                            if( i>0 && i<width-1 &&j >0 &&  j <height-1)
                                                cells[i][j].setState(Color.BLACK);

                                        }
                                    }
                                }
                                currentNumber++;
                                inclusions++;

                            }
                        }


                    }
                }
            }

        }



        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
            }
        }
        System.out.println("Inclusions "+inclusions);
    }

    public void removeNonSelectedGrains(List<Color> selectedGrains, String type){
        if(type.equalsIgnoreCase("Substructure")) {
            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < height - 1; j++) {
                    if (!selectedGrains.contains(cellsOld[i][j].getState())) {
                        if (!cellsOld[i][j].getState().equals(Color.BLACK)) {
                            cells[i][j].setState(Color.WHITE);
                        }
                    } else
                        cells[i][j].setPhase(2);
                }
            }
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    cellsOld[i][j].setState(cells[i][j].getState());
                    cellsOld[i][j].setPhase(cells[i][j].getPhase());
                }
            }
        }
        else if(type.equalsIgnoreCase("Dual-Phase")){
            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < height - 1; j++) {
                    if (!selectedGrains.contains(cellsOld[i][j].getState())) {
                        if (!cellsOld[i][j].getState().equals(Color.BLACK)) {
                            cells[i][j].setState(Color.WHITE);
                        }
                    } else {
                        cells[i][j].setPhase(2);
                        cells[i][j].setState(Color.DEEPPINK);
                    }
                }
            }
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cellsOld[i][j].setState(cells[i][j].getState());
                    cellsOld[i][j].setPhase(cells[i][j].getPhase());
                }
            }
        }
    }

    public void clearSelectedSpace(List<Color> selectedGrains){
        boolean boundary=false;
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                if (selectedGrains.contains(cellsOld[i][j].getState()))
                {       boundary = false;
                if (!cellsOld[i][j].getState().equals(cellsOld[i][j + 1].getState()))
                    boundary = true;
                else if (!cellsOld[i][j].getState().equals(cellsOld[i][j - 1].getState()))
                    boundary = true;
                else if (!cellsOld[i][j].getState().equals(cellsOld[i + 1][j - 1].getState()))
                    boundary = true;
                else if (!cellsOld[i][j].getState().equals(cellsOld[i + 1][j].getState()))
                    boundary = true;
                else if (!cellsOld[i][j].getState().equals(cellsOld[i + 1][j + 1].getState()))
                    boundary = true;
                else if (!cellsOld[i][j].getState().equals(cellsOld[i - 1][j - 1].getState()))
                    boundary = true;
                else if (!cellsOld[i][j].getState().equals(cellsOld[i - 1][j].getState()))
                    boundary = true;
                else if (!cellsOld[i][j].getState().equals(cellsOld[i - 1][j + 1].getState()))
                    boundary = true;
                if (boundary)
                    cells[i][j].setPhase(10);
            }
            }
        }

        //update
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
                cellsOld[i][j].setPhase(cells[i][j].getPhase());
            }
        }

        //change colors
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                if(cellsOld[i][j].getPhase()!=10) {
                    cells[i][j].setState(Color.WHITE);
                    cells[i][j].setPhase(0);

                }
                else
                    cells[i][j].setState(Color.BLACK);
            }
        }
        //update
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
                cellsOld[i][j].setPhase(cells[i][j].getPhase());
            }
        }


    }

    public void clearSpace() {
        //setting phase
        boolean boundary=false;
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                boundary=false;
                if(!cellsOld[i][j].getState().equals(cellsOld[i][j+1].getState()))
                    boundary=true;
                else if(!cellsOld[i][j].getState().equals(cellsOld[i][j-1].getState()))
                    boundary=true;
                else if(!cellsOld[i][j].getState().equals(cellsOld[i+1][j-1].getState()))
                    boundary=true;
                else if(!cellsOld[i][j].getState().equals(cellsOld[i+1][j].getState()))
                    boundary=true;
                else if(!cellsOld[i][j].getState().equals(cellsOld[i+1][j+1].getState()))
                    boundary=true;
                else if(!cellsOld[i][j].getState().equals(cellsOld[i-1][j-1].getState()))
                    boundary=true;
                else if(!cellsOld[i][j].getState().equals(cellsOld[i-1][j].getState()))
                    boundary=true;
                else if(!cellsOld[i][j].getState().equals(cellsOld[i-1][j+1].getState()))
                    boundary=true;
                if(boundary)
                    cells[i][j].setPhase(10);
            }
        }

//        //update
//        for(int i=0;i<width;i++){
//            for(int j=0;j<height;j++){
//                cellsOld[i][j].setState(cells[i][j].getState());
//                cellsOld[i][j].setPhase(cells[i][j].getPhase());
//            }
//        }
//
//        //change colors
//        for(int i=0;i<width;i++){
//            for(int j=0;j<height;j++){
//                if(cellsOld[i][j].getPhase()!=10) {
//                    cellsOld[i][j].setState(Color.WHITE);
//                    cellsOld[i][j].setPhase(0);
//
//                }
//                else
//                    cellsOld[i][j].setState(Color.BLACK);
//            }
//        }

        //update
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
                cellsOld[i][j].setPhase(cells[i][j].getPhase());
            }
        }

        //change colors
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                if(cellsOld[i][j].getPhase()!=10) {
                    cells[i][j].setState(Color.WHITE);
                    cells[i][j].setPhase(0);

                }
                else
                    cells[i][j].setState(Color.BLACK);
            }
        }
        //update
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
                cellsOld[i][j].setPhase(cells[i][j].getPhase());
            }
        }
    }

    public void initMC(int numberOfStates){
        Random rng=new Random();

        while(colorsMc.size()<numberOfStates) {
            Color newState= Color.rgb(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256));
            if(!colorsMc.contains(newState) && !newState.equals(Color.WHITE) && !newState.equals(Color.BLACK)){
                colorsMc.add(newState);
            }
        }


        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                if(cells[i][j].getState().equals(Color.WHITE)) {
                    cells[i][j].setState(colorsMc.get(rng.nextInt(colorsMc.size())));
                    ///888  - MC
                    cells[i][j].setPhase(888);

                    //           cells[i][j].setMCDone(true);
                }
            }
        }
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cellsOld[i][j].setState(cells[i][j].getState());
                cellsOld[i][j].setPhase(cells[i][j].getPhase());
 //               cellsOld[i][j].setMCDone(cells[i][j].isMCDone());
//                cells[i][j].setMCDone(true);
            }
        }

    }

    public int calculateEnergy(int x, int y){
        int energy=0;
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i==0 && j==0) continue;
                if(!cellsOld[x+i][y+j].getState().equals(cellsOld[x][y].getState()))
                    energy++;
            }
        }
        return energy;
    }

    public void clearBorders(){
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                if(i==0 || j==0 || i==(width-1) || j==(height-1)){ //&& dodac
//                    System.out.println(i+" "+j);
                    cellsOld[i][j].setState(Color.WHITE);
                    cells[i][j].setState(Color.WHITE);
                    cellsOld[i][j].setPhase(0);
                    cells[i][j].setPhase(0);

                }
            }
        }
    //System.out.println(width+" "+height);
}

    public void updateCells(){    for(int i=0;i<width;i++){
    for(int j=0;j<height;j++){
        cellsOld[i][j].setState(cells[i][j].getState());
        cellsOld[i][j].setPhase(cells[i][j].getPhase());
    }
}}

    public void nextMCSteep() {
        //uzywac tylko cellsOld
        //sprawdzac losowo nie w petli po kolei!!.
        //dodac do gui




        Random rng = new Random();
//        for(int i=1;i<width-1;i++){
//            for(int j=1;j<height-1;j++){
//                int energyOld=calculateEnergy(i,j);
//                Color stateOld=cellsOld[i][j].getState();
//                cellsOld[i][j].setState(colorsMc.get(rng.nextInt(colorsMc.size())));
//                int energyNew=calculateEnergy(i,j);
//                if(energyNew>energyOld){
//                    cellsOld[i][j].setState(stateOld);
//                }
//            }
//        }


        List<Point> pointsList = new ArrayList<>();
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                if(!cellsOld[i][j].getState().equals(Color.DEEPPINK) && !cellsOld[i][j].getState().equals(Color.BLACK) && !cells[i][j].getState().equals(Color.DEEPPINK) && !cellsOld[i][j].getState().equals(Color.BLACK))
                    pointsList.add(new Point(i, j));
            }
        }

        //v2
        Collections.shuffle(pointsList);

        for (int i = 0; i < pointsList.size(); i++) {
            //int id=rng.nextInt(pointsList.size());
            //Point point=pointsList.get(id);
            //pointsList.remove(id);

            //v2 b
            Point point = pointsList.get(i);
            int x, y;
            while (true) {

                x = rng.nextInt(3) - 1;
                y = rng.nextInt(3) - 1;
                if (x != 0 && y != 0 && !cellsOld[point.x + x][point.y + y].getState().equals(Color.DEEPPINK))
                    break;
            }

            //v2 e
            //if (!cellsOld[point.x][point.y].getState().equals(Color.BLACK) && !cellsOld[point.x][point.y].getState().equals(Color.PINK)) {
                int energyOld = calculateEnergy(point.x, point.y);
                Color stateOld = cellsOld[point.x][point.y].getState();
//                cellsOld[point.x][point.y].setState(colorsMc.get(rng.nextInt(colorsMc.size())));

                cellsOld[point.x][point.y].setState(cellsOld[point.x + x][point.y + y].getState());

                int energyNew = calculateEnergy(point.x, point.y);
                if (energyNew > energyOld) {
                    cellsOld[point.x][point.y].setState(stateOld);
                }
            //}
            }


    }


    private boolean isOnBorder(int x,int y){
        boolean answer=false;
        Color color=cellsOld[x][y].getState();
        for(int i=x-1;i<=x+1;i++){
            for(int j=y-1;j<=y+1;j++){
                if(i!=0 && j!=0 && i!=width-1 && j!=height-1) {
                    if (!cellsOld[i][j].getState().equals(color)) {
                        answer = true;
                        break;
                    }
                }
            }
        }
        return answer;
    }

    public void recalculateHEnergy(int min, int max){
        for(int i=1;i<width-1;i++) {
            for (int j = 1; j < height - 1; j++) {
                    if(cellsOld[i][j].isRecrystalized()) cellsOld[i][j].setH(0);
                    else if(isOnBorder(i,j)) cellsOld[i][j].setH(max);
                    else cellsOld[i][j].setH(min);
            }
        }
    }

    private void seedRecrysallNucelons(List<Point> nonRecrystallList, String nucelonsLocation, int number){
        if(nucelonsLocation.equalsIgnoreCase("Anywhere")){
            for(int i=0;i<number;i++){
                Point point=nonRecrystallList.get(i);
                cellsOld[point.x][point.y].setRecrystalized(true);
                cellsOld[point.x][point.y].setH(0);
            }
        }

    if(nucelonsLocation.equalsIgnoreCase("GB")){
        int j=0;
        for(int i=0;i<nonRecrystallList.size();i++){
            Point point=nonRecrystallList.get(i);
            if(isOnBorder(point.x,point.y)) {
                cellsOld[point.x][point.y].setRecrystalized(true);
                cellsOld[point.x][point.y].setH(0);
                j++;
            }
            if(j>=number) break;
        }
    }
    }

    public void recrystall(int iterationCounter, int numberOfNucleons, String nucleationType, String nucelonsLocation, int maxIterations){
        //is all recrysalized? w liscie
        Random rng = new Random();

        //List<Point> recrystalizedPointsList = new ArrayList<>();
        List<Point> nonRecrystalizedPointsList = new ArrayList<>();

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                if(!cellsOld[i][j].isRecrystalized())
                    nonRecrystalizedPointsList.add(new Point(i, j));
                //else
                  // nonRecrystalizedPointsList.add(new Point(i, j));
            }
        }

        //try to seed
        //fixme: number of nucelons
        System.out.println("\n\t"+nonRecrystalizedPointsList.size());
        if(nonRecrystalizedPointsList.size()>numberOfNucleons){
            Collections.shuffle(nonRecrystalizedPointsList);
            //todo: add rest of types
            if(nucleationType.equalsIgnoreCase("At the begining of simulation") && iterationCounter==0){
                seedRecrysallNucelons(nonRecrystalizedPointsList,nucelonsLocation,numberOfNucleons);
            }
            else if(nucleationType.equalsIgnoreCase("Constant")){
                int nucelonsPart=(int)(numberOfNucleons/maxIterations);
                seedRecrysallNucelons(nonRecrystalizedPointsList,nucelonsLocation,nucelonsPart);

            }
            //increasing
            else{
                int nucelonsPart=(int)(numberOfNucleons/maxIterations);
//                if(iterationCounter<4){
                    seedRecrysallNucelons(nonRecrystalizedPointsList,nucelonsLocation,(int)(nucelonsPart*(iterationCounter+1)));

//                }
            }
        }
        //seed end

        //growth start
        List<Point> nonRecrystalizedPointsList2 = new ArrayList<>();

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                if (!cellsOld[i][j].isRecrystalized())
                    nonRecrystalizedPointsList2.add(new Point(i, j));
            }
        }
        Collections.shuffle(nonRecrystalizedPointsList2);

        for(int i=0;i<nonRecrystalizedPointsList2.size();i++){
            Point point=nonRecrystalizedPointsList2.get(i);
            if(hasRecrystallizedNeighbour(point)){
                int oldEnergy=calculateEnergy(point.x,point.y)+cellsOld[point.x][point.y].getH();

                Color oldColor=cellsOld[point.x][point.y].getState();
                Color newColor=randomRecrystallState(point);

                cellsOld[point.x][point.y].setState(newColor);
                int newEnergy=calculateEnergy(point.x,point.y);


                //warunek na zmiane
                if(newEnergy<=(oldEnergy) ){
                    cellsOld[point.x][point.y].setRecrystalized(true);
                    cellsOld[point.x][point.y].setH(0);
                    //nie trzeba wrocic do koloru?
                }
                else{
                    cellsOld[point.x][point.y].setState(oldColor);
                }
            }

        }








    }

    private boolean hasRecrystallizedNeighbour(Point point){
        boolean result=false;

        for(int i=point.x-1;i<=point.x+1;i++){
            for(int j=point.y-1;j<=point.y+1;j++){
                if(i!=0 && j!=0 && i!=width-1 && j!=height-1) {
                    if (cellsOld[i][j].isRecrystalized()) {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }

    private Color randomRecrystallState(Point point){
        List<Color> colors=new ArrayList<>();
        for(int i=point.x-1;i<=point.x+1;i++) {
            for (int j = point.y - 1; j <= point.y + 1; j++) {
                    if(!colors.contains(cellsOld[i][j].getState()) && cellsOld[i][j].isRecrystalized())
                        colors.add(cellsOld[i][j].getState());
            }
        }
        //colors.remove(cellsOld[point.x][point.y].getState());
        if(colors.size()>0){
            Collections.shuffle(colors);
            return colors.get(0);
        }
        else
            return cellsOld[point.x][point.y].getState();
    }
    private Color randomRecrystallState2(Point point){
        List<Color> colors=new ArrayList<>();
        for(int i=0;i<colorsMc.size();i++) {

                    colors.add(colorsMc.get(i));

        }
        //colors.remove(cellsOld[point.x][point.y].getState());
        if(colors.size()>0){
            Collections.shuffle(colors);
            return colors.get(0);
        }
        else
            return cellsOld[point.x][point.y].getState();
    }
}