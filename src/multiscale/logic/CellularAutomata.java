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
                if(extendedMoore) extendedMoore(i,j);
                else vonNeumann(i,j);
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
}