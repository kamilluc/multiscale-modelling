import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.exp;
import static java.lang.Math.sqrt;

/**
 * Created by kamil on 03.05.2017.
 */
public class Logic {

    public Cell[][] map,newmap;
    private int width,height;
    private int firstGeneration;
    private int neigh=0;
    boolean choice=false;
    boolean periodic=true;
    int seedRule=0;
    int radius=1;
    boolean recrystalization;
    static int recrystalCounter=0;
    static double roAll=0;
    static double k=1000;
    private int mcIterations=10;
    private int numberOfStates=50;
    private int newMc=1;
    private boolean firstSteepMc=true;

    ArrayList<CellIndex> outsideSeeds;
    ArrayList<CellIndex> insideSeeds;
    ArrayList<Color> mcStates;
    ArrayList<CellIndex> allSeeds;

    boolean lollol=false;


    public void saveToFile(){
        try {
            PrintWriter out = new PrintWriter("filename.txt");
            for(Cell[] x:map){
                for(Cell y:x){
                    out.println(y);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Logic(int width, int height, int firstGeneration) {
        this.width = (width+2);
        this.height = (height+2);
        this.firstGeneration = firstGeneration;
        map=new Cell[height+2][width+2];
        newmap=new Cell[height+2][width+2];
    }

    public int getMcIterations() {
        return mcIterations;
    }

    public void setMcIterations(int mcIterations) {
        this.mcIterations = mcIterations;
    }

    public int getNumberOfStates() {
        return numberOfStates;
    }

    public void setNumberOfStates(int numberOfStates) {
        this.numberOfStates = numberOfStates;
    }

    public int getNewMc() {
        return newMc;
    }

    public void setNewMc(int newMc) {
        this.newMc = newMc;
    }

    public void show(){
        for(int i=1;i<height-1;i++){
            for(int j=1;j<width-1;j++){
                if(map[i][j].isState())
                    System.out.print("#");
                else
                    System.out.print(" ");
            }
            System.out.println();
        }
    }

    private void updateMap(){
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                //map[i][j]=newmap[i][j];

                map[i][j].setState(newmap[i][j].isState());
                map[i][j].setColor(newmap[i][j].getColor());
                map[i][j].setRecrystalized(newmap[i][j].isRecrystalized());
                map[i][j].setRo(newmap[i][j].getRo());
            }
        }
    }

    public void newSeed(int x,int y){
        map[y + 1][x + 1].setState(true);
        newmap[y + 1][x + 1].setState(true);
     //  newmap[y + 1][x + 1].setState(true);
        Random rng=new Random();
        map[y + 1][x + 1].setColor(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
        newmap[y + 1][x + 1].setColor(map[y + 1][x + 1].getColor());
    //    newmap[y + 1][x + 1].setColor(map[y + 1][x + 1].getColor());

    }

    public void start() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = new Cell();
                newmap[i][j] = new Cell();
            }
        }

        if(seedRule==0) {
            Random rng = new Random();
            for (int i = 0; i < firstGeneration; i++) {
//                int x = rng.nextInt(width - 2);
//                int y = rng.nextInt(height - 2);
                int x=rng.nextInt((width-1)-1);
                int y=rng.nextInt((height-1)-1);
                map[y + 1][x + 1].setState(true);
                newmap[y + 1][x + 1].setState(true);
                map[y + 1][x + 1].setColor(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
                newmap[y + 1][x + 1].setColor(map[y + 1][x + 1].getColor());
            }
        }
        else if(seedRule==1){
            Random rng = new Random();
            double formula=sqrt(width*height/firstGeneration);
            int iIterations=(int)(height/formula);
            int jIterations=(int)(width/formula);
            //int space=(int)(formula/2*1.5);
            int spaceX=width/(jIterations+1);
            int spaceY=height/(iIterations+1);
            for(int i=0;i<iIterations;i++){
                for(int j=0;j<jIterations;j++){
                    int x=(int)(spaceX+j*spaceX);
                    int y=(int)(spaceY+i*spaceY);
                    map[y][x].setState(true);
                    newmap[y][x].setState(true);
                    map[y][x].setColor(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
                    newmap[y][x].setColor(map[y][x].getColor());
                }
            }
//            int spaceX=0
//            int spaceY=0;
//            for (int i = 0; i < firstGeneration; i++) {
//                int x = i*spaceX+spaceX;
//                int y = i*spaceY+spaceY;
//                map[y + 1][x + 1].setState(true);
//                newmap[y + 1][x + 1].setState(true);
//                map[y + 1][x + 1].setColor(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
//                newmap[y + 1][x + 1].setColor(map[y + 1][x + 1].getColor());
        }
        else if(seedRule==2) {
            Random rng = new Random();
            int x,y;
            for(int i=0;i<firstGeneration;i++){
                x=rng.nextInt((width-1)-1);
                y=rng.nextInt((height-1)-1);
                if(!map[y][x].isRadiusMark()){
                    map[y][x].setState(true);
                    newmap[y][x].setState(true);
                    map[y][x].setColor(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
                    newmap[y][x].setColor(map[y][x].getColor());
                    for(int j=-radius;j<radius;j++){
                        for(int k=-radius;k<radius;k++){
                            if((y+j)<1 || (y+j)>(height-1) || (x+k)<1 || (x+k)>(width-1))
                                continue;
                            map[y+j][x+k].setRadiusMark(true);
                            newmap[y+j][x+k].setRadiusMark(true);
                        }
                    }

                }
                else
                    continue;
            }
        }
//
//        for(int i=0;i<height;i++){
//            for(int j=0;j<width;j++){
//                newmap[i][j].setState(map[i][j].isState());
//                newmap[i][j].setColor(map[i][j].getColor());
//            }
//        }
    }

    public int emptyFields(){
        int x=0;
        for(int i=1;i<(height-1);i++) {
            for (int j = 1; j < (width - 1); j++) {
                if (!map[i][j].isState()) x++;
            }
        }
        return x;
    }

    private void moore(int i, int j){
        if(map[i][j].isState()) {
            if(!map[i-1][j-1].isState()){
                newmap[i-1][j-1].setColor(map[i][j].getColor());
                newmap[i-1][j-1].setState(true);
            }
            if(!map[i-1][j].isState()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setState(true);
            }
            if(!map[i-1][j+1].isState()){
                newmap[i-1][j+1].setColor(map[i][j].getColor());
                newmap[i-1][j+1].setState(true);
            }
            if(!map[i][j-1].isState()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setState(true);
            }
            if(!map[i][j+1].isState()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setState(true);
            }
            if(!map[i+1][j-1].isState()){
                newmap[i+1][j-1].setColor(map[i][j].getColor());
                newmap[i+1][j-1].setState(true);
            }
            if(!map[i+1][j].isState()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setState(true);
            }
            if(!map[i+1][j+1].isState()){
                newmap[i+1][j+1].setColor(map[i][j].getColor());
                newmap[i+1][j+1].setState(true);
            }
    }
    }

    private void moore2(int i, int j){
        if(map[i][j].isRecrystalized()) {
            if(!map[i-1][j-1].isRecrystalized()){
                newmap[i-1][j-1].setColor(map[i][j].getColor());
                newmap[i-1][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i-1][j].isRecrystalized()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i-1][j+1].isRecrystalized()){
                newmap[i-1][j+1].setColor(map[i][j].getColor());
                newmap[i-1][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

            if(!map[i][j-1].isRecrystalized()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i][j+1].isRecrystalized()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

            if(!map[i+1][j-1].isRecrystalized()){
                newmap[i+1][j-1].setColor(map[i][j].getColor());
                newmap[i+1][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i+1][j].isRecrystalized()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i+1][j+1].isRecrystalized()){
                newmap[i+1][j+1].setColor(map[i][j].getColor());
                newmap[i+1][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

        }
    }

    private void neumann(int i, int j){
        if(map[i][j].isState()) {
            if(!map[i-1][j].isState()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setState(true);
            }
            if(!map[i][j-1].isState()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setState(true);
            }
            if(!map[i][j+1].isState()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setState(true);
            }
            if(!map[i+1][j].isState()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setState(true);
            }
        }
    }

    private void neumann2(int i, int j){
        if(map[i][j].isRecrystalized()) {
//            if(!map[i-1][j-1].isRecrystalized()){
//                newmap[i-1][j-1].setColor(map[i][j].getColor());
//                newmap[i-1][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
            if(!map[i-1][j].isRecrystalized()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
//            if(!map[i-1][j+1].isRecrystalized()){
//                newmap[i-1][j+1].setColor(map[i][j].getColor());
//                newmap[i-1][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

            if(!map[i][j-1].isRecrystalized()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i][j+1].isRecrystalized()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

//            if(!map[i+1][j-1].isRecrystalized()){
//                newmap[i+1][j-1].setColor(map[i][j].getColor());
//                newmap[i+1][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
            if(!map[i+1][j].isRecrystalized()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
//            if(!map[i+1][j+1].isRecrystalized()){
//                newmap[i+1][j+1].setColor(map[i][j].getColor());
//                newmap[i+1][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

        }
    }

    private void hexaLeft(int i, int j){
        if(map[i][j].isState()) {
            if(!map[i-1][j-1].isState()){
                newmap[i-1][j-1].setColor(map[i][j].getColor());
                newmap[i-1][j-1].setState(true);
            }
            if(!map[i-1][j].isState()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setState(true);
            }
            if(!map[i][j-1].isState()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setState(true);
            }
            if(!map[i][j+1].isState()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setState(true);
            }
            if(!map[i+1][j].isState()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setState(true);
            }
            if(!map[i+1][j+1].isState()){
                newmap[i+1][j+1].setColor(map[i][j].getColor());
                newmap[i+1][j+1].setState(true);
            }
        }
    }

    private void hexaRight(int i, int j){
        if(map[i][j].isState()) {
            if(!map[i-1][j].isState()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setState(true);
            }
            if(!map[i-1][j+1].isState()){
                newmap[i-1][j+1].setColor(map[i][j].getColor());
                newmap[i-1][j+1].setState(true);
            }
            if(!map[i][j-1].isState()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setState(true);
            }
            if(!map[i][j+1].isState()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setState(true);
            }
            if(!map[i+1][j-1].isState()){
                newmap[i+1][j-1].setColor(map[i][j].getColor());
                newmap[i+1][j-1].setState(true);
            }
            if(!map[i+1][j].isState()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setState(true);
            }
        }
    }

    private void hexaLeft2(int i, int j){
        if(map[i][j].isRecrystalized()) {
            if(!map[i-1][j-1].isRecrystalized()){
                newmap[i-1][j-1].setColor(map[i][j].getColor());
                newmap[i-1][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i-1][j].isRecrystalized()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
//            if(!map[i-1][j+1].isRecrystalized()){
//                newmap[i-1][j+1].setColor(map[i][j].getColor());
//                newmap[i-1][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

            if(!map[i][j-1].isRecrystalized()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i][j+1].isRecrystalized()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

//            if(!map[i+1][j-1].isRecrystalized()){
//                newmap[i+1][j-1].setColor(map[i][j].getColor());
//                newmap[i+1][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
            if(!map[i+1][j].isRecrystalized()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i+1][j+1].isRecrystalized()){
                newmap[i+1][j+1].setColor(map[i][j].getColor());
                newmap[i+1][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

        }
    }

    private void hexaRight2(int i, int j){
        if(map[i][j].isRecrystalized()) {
//            if(!map[i-1][j-1].isRecrystalized()){
//                newmap[i-1][j-1].setColor(map[i][j].getColor());
//                newmap[i-1][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
            if(!map[i-1][j].isRecrystalized()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i-1][j+1].isRecrystalized()){
                newmap[i-1][j+1].setColor(map[i][j].getColor());
                newmap[i-1][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

            if(!map[i][j-1].isRecrystalized()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i][j+1].isRecrystalized()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

            if(!map[i+1][j-1].isRecrystalized()){
                newmap[i+1][j-1].setColor(map[i][j].getColor());
                newmap[i+1][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i+1][j].isRecrystalized()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
//            if(!map[i+1][j+1].isRecrystalized()){
//                newmap[i+1][j+1].setColor(map[i][j].getColor());
//                newmap[i+1][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

        }
    }

    private void pentaLeft(int i, int j){
        if(map[i][j].isState()) {
            if(!map[i-1][j-1].isState()){
                newmap[i-1][j-1].setColor(map[i][j].getColor());
                newmap[i-1][j-1].setState(true);
            }
            if(!map[i-1][j].isState()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setState(true);
            }

            if(!map[i][j-1].isState()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setState(true);
            }

            if(!map[i+1][j-1].isState()){
                newmap[i+1][j-1].setColor(map[i][j].getColor());
                newmap[i+1][j-1].setState(true);
            }
            if(!map[i+1][j].isState()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setState(true);
            }
                    }
    }

    private void pentaRight(int i, int j){
        if(map[i][j].isState()) {

            if(!map[i-1][j].isState()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setState(true);
            }
            if(!map[i-1][j+1].isState()){
                newmap[i-1][j+1].setColor(map[i][j].getColor());
                newmap[i-1][j+1].setState(true);
            }
            if(!map[i][j+1].isState()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setState(true);
            }
            if(!map[i+1][j].isState()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setState(true);
            }
            if(!map[i+1][j+1].isState()){
                newmap[i+1][j+1].setColor(map[i][j].getColor());
                newmap[i+1][j+1].setState(true);
            }
        }
    }

    private void pentaTop(int i, int j){
        if(map[i][j].isState()) {
//            if(!map[i-1][j-1].isState()){
//                newmap[i-1][j-1].setColor(map[i][j].getColor());
//                newmap[i-1][j-1].setState(true);
//            }
//            if(!map[i-1][j].isState()){
//                newmap[i-1][j].setColor(map[i][j].getColor());
//                newmap[i-1][j].setState(true);
//            }
//            if(!map[i-1][j+1].isState()){
//                newmap[i-1][j+1].setColor(map[i][j].getColor());
//                newmap[i-1][j+1].setState(true);
//            }
            if(!map[i][j-1].isState()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setState(true);
            }
            if(!map[i][j+1].isState()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setState(true);
            }
            if(!map[i+1][j-1].isState()){
                newmap[i+1][j-1].setColor(map[i][j].getColor());
                newmap[i+1][j-1].setState(true);
            }
            if(!map[i+1][j].isState()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setState(true);
            }
            if(!map[i+1][j+1].isState()){
                newmap[i+1][j+1].setColor(map[i][j].getColor());
                newmap[i+1][j+1].setState(true);
            }
        }
    }

    private void pentaBottom(int i, int j){
        if(map[i][j].isState()) {
            if(!map[i-1][j-1].isState()){
                newmap[i-1][j-1].setColor(map[i][j].getColor());
                newmap[i-1][j-1].setState(true);
            }
            if(!map[i-1][j].isState()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setState(true);
            }
            if(!map[i-1][j+1].isState()){
                newmap[i-1][j+1].setColor(map[i][j].getColor());
                newmap[i-1][j+1].setState(true);
            }
            if(!map[i][j-1].isState()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setState(true);
            }
            if(!map[i][j+1].isState()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setState(true);
            }
//            if(!map[i+1][j-1].isState()){
//                newmap[i+1][j-1].setColor(map[i][j].getColor());
//                newmap[i+1][j-1].setState(true);
//            }
//            if(!map[i+1][j].isState()){
//                newmap[i+1][j].setColor(map[i][j].getColor());
//                newmap[i+1][j].setState(true);
//            }
//            if(!map[i+1][j+1].isState()){
//                newmap[i+1][j+1].setColor(map[i][j].getColor());
//                newmap[i+1][j+1].setState(true);
//            }
        }
    }

    private void pentaLeft2(int i, int j){
        if(map[i][j].isRecrystalized()) {
            if(!map[i-1][j-1].isRecrystalized()){
                newmap[i-1][j-1].setColor(map[i][j].getColor());
                newmap[i-1][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i-1][j].isRecrystalized()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
//            if(!map[i-1][j+1].isRecrystalized()){
//                newmap[i-1][j+1].setColor(map[i][j].getColor());
//                newmap[i-1][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

            if(!map[i][j-1].isRecrystalized()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
//            if(!map[i][j+1].isRecrystalized()){
//                newmap[i][j+1].setColor(map[i][j].getColor());
//                newmap[i][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

            if(!map[i+1][j-1].isRecrystalized()){
                newmap[i+1][j-1].setColor(map[i][j].getColor());
                newmap[i+1][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i+1][j].isRecrystalized()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
//            if(!map[i+1][j+1].isRecrystalized()){
//                newmap[i+1][j+1].setColor(map[i][j].getColor());
//                newmap[i+1][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

        }
    }

    private void pentaRight2(int i, int j){
        if(map[i][j].isRecrystalized()) {
//            if(!map[i-1][j-1].isRecrystalized()){
//                newmap[i-1][j-1].setColor(map[i][j].getColor());
//                newmap[i-1][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
            if(!map[i-1][j].isRecrystalized()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i-1][j+1].isRecrystalized()){
                newmap[i-1][j+1].setColor(map[i][j].getColor());
                newmap[i-1][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

//            if(!map[i][j-1].isRecrystalized()){
//                newmap[i][j-1].setColor(map[i][j].getColor());
//                newmap[i][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
            if(!map[i][j+1].isRecrystalized()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

//            if(!map[i+1][j-1].isRecrystalized()){
//                newmap[i+1][j-1].setColor(map[i][j].getColor());
//                newmap[i+1][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
            if(!map[i+1][j].isRecrystalized()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i+1][j+1].isRecrystalized()){
                newmap[i+1][j+1].setColor(map[i][j].getColor());
                newmap[i+1][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

        }
    }

    private void pentaTop2(int i, int j){
        if(map[i][j].isRecrystalized()) {
//            if(!map[i-1][j-1].isRecrystalized()){
//                newmap[i-1][j-1].setColor(map[i][j].getColor());
//                newmap[i-1][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
//            if(!map[i-1][j].isRecrystalized()){
//                newmap[i-1][j].setColor(map[i][j].getColor());
//                newmap[i-1][j].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
//            if(!map[i-1][j+1].isRecrystalized()){
//                newmap[i-1][j+1].setColor(map[i][j].getColor());
//                newmap[i-1][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

            if(!map[i][j-1].isRecrystalized()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i][j+1].isRecrystalized()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

            if(!map[i+1][j-1].isRecrystalized()){
                newmap[i+1][j-1].setColor(map[i][j].getColor());
                newmap[i+1][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i+1][j].isRecrystalized()){
                newmap[i+1][j].setColor(map[i][j].getColor());
                newmap[i+1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i+1][j+1].isRecrystalized()){
                newmap[i+1][j+1].setColor(map[i][j].getColor());
                newmap[i+1][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

        }
    }

    private void pentaBottom2(int i, int j){
        if(map[i][j].isRecrystalized()) {
            if(!map[i-1][j-1].isRecrystalized()){
                newmap[i-1][j-1].setColor(map[i][j].getColor());
                newmap[i-1][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i-1][j].isRecrystalized()){
                newmap[i-1][j].setColor(map[i][j].getColor());
                newmap[i-1][j].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i-1][j+1].isRecrystalized()){
                newmap[i-1][j+1].setColor(map[i][j].getColor());
                newmap[i-1][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

            if(!map[i][j-1].isRecrystalized()){
                newmap[i][j-1].setColor(map[i][j].getColor());
                newmap[i][j-1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }
            if(!map[i][j+1].isRecrystalized()){
                newmap[i][j+1].setColor(map[i][j].getColor());
                newmap[i][j+1].setRecrystalized(true);
                newmap[i-1][j-1].setRo(0.0);
            }

//            if(!map[i+1][j-1].isRecrystalized()){
//                newmap[i+1][j-1].setColor(map[i][j].getColor());
//                newmap[i+1][j-1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
//            if(!map[i+1][j].isRecrystalized()){
//                newmap[i+1][j].setColor(map[i][j].getColor());
//                newmap[i+1][j].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }
//            if(!map[i+1][j+1].isRecrystalized()){
//                newmap[i+1][j+1].setColor(map[i][j].getColor());
//                newmap[i+1][j+1].setRecrystalized(true);
//                newmap[i-1][j-1].setRo(0.0);
//            }

        }
    }

    private void makePeriodic(){
        for(int i=1;i<(width-1);i++) {
//            map[0][i] = map[height-2][i];
            //          map[height-1][i]=map[1][i];
            newmap[0][i] = newmap[height-2][i];
            newmap[height-1][i]=newmap[1][i];

        }
        for(int i=1;i<(height-1);i++){
//            map[i][0]=map[i][width-2];
            //          map[i][width-1]=map[i][1];
            newmap[i][0]=newmap[i][width-2];
            newmap[i][width-1]=newmap[i][1];
// z prawej na lewo dziala
            // z dolu na gore tez
        }
    }

    public void nextStep(){

        if(periodic)
            makePeriodic();


//        for(int i=0;i<width;i++) {
//            map[0][i] = map[height-2][i];
//            map[height-1][i]=map[1][i];
//        }
//        for(int i=1;i<(height-1);i++){
//            map[i][0]=map[i][width-2];
//            map[i][width-1]=map[i][1];
//        }
        if((recrystalization==true)&&(emptyFields()==0)) {
            recrystalization();
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                   // if (neigh == 0) moore2(i, j);
                   // moore2(i, j);
                    if(neigh==0) moore2(i,j);
                    else if(neigh==1) neumann2(i,j);
                    else if(neigh==2) hexaLeft2(i,j);
                    else if(neigh==3) hexaRight2(i,j);
                    else if(neigh==4) {
                        boolean tmp=randHex();
                        if(tmp) hexaLeft2(i,j);
                        else hexaRight2(i,j);
//                    if(!choice) hexaLeft(i,j);
//                    else if(choice) hexaRight(i,j);
                    }
//                else if(neigh==5) {
//                    if(!choice) pentLeft(i,j);
//                    else if(choice) pentRight(i,j);
//                }
                    else if(neigh==5) {
                        //pentLeft(i,j);
                        int tmp=randPenta();
                        if(tmp==0) pentaBottom2(i,j);
                        else if(tmp==1) pentaTop2(i,j);
                        else if(tmp==2) pentaLeft2(i,j);
                        else if(tmp==3) pentaRight2(i,j);
                    }
//                    else if (neigh == 1) neumann(i, j);
//                    else if (neigh == 2) hexaLeft(i, j);
//                    else if (neigh == 3) hexaRight(i, j);
//                    else if (neigh == 4) {
//                        boolean tmp = randHex();
//                        if (tmp) hexaLeft(i, j);
//                        else hexaRight(i, j);
////                    if(!choice) hexaLeft(i,j);
////                    else if(choice) hexaRight(i,j);
//                    }
////                else if(neigh==5) {
////                    if(!choice) pentLeft(i,j);
////                    else if(choice) pentRight(i,j);
////                }
//                    else if (neigh == 5) {
//                        //pentLeft(i,j);
//                        int tmp = randPenta();
//                        if (tmp == 0) pentaBottom(i, j);
//                        else if (tmp == 1) pentaTop(i, j);
//                        else if (tmp == 2) pentaLeft(i, j);
//                        else if (tmp == 3) pentaRight(i, j);
//                    }
                }
            }
        }



        if(newMc==1 && firstSteepMc==false){
            System.out.println(mcIterations);
            mcIterations--;
            if(mcIterations>0)
            mcNext();
            else
                System.out.println("Koniec MC");


        }

        if(newMc==1 && firstSteepMc){
            mc();
            firstSteepMc=false;
        }

        if(newMc==2 && (emptyFields()==0)){
            //System.out.println(mcIterations);
            mcIterations--;
            if(mcIterations>0)
                mcNext();
            else {
                System.out.println("Koniec MC");

            }

        }

        if(newMc==0 || (newMc==2 && emptyFields()!=0)){

        for(int i=1;i<height-1;i++){
            for(int j=1;j<width-1;j++){
                if(neigh==0) moore(i,j);
                else if(neigh==1) neumann(i,j);
                else if(neigh==2) hexaLeft(i,j);
                else if(neigh==3) hexaRight(i,j);
                else if(neigh==4) {
                            boolean tmp=randHex();
                            if(tmp) hexaLeft(i,j);
                            else hexaRight(i,j);
//                    if(!choice) hexaLeft(i,j);
//                    else if(choice) hexaRight(i,j);
                }
//                else if(neigh==5) {
//                    if(!choice) pentLeft(i,j);
//                    else if(choice) pentRight(i,j);
//                }
                else if(neigh==5) {
                    //pentLeft(i,j);
                    int tmp=randPenta();
                    if(tmp==0) pentaBottom(i,j);
                    else if(tmp==1) pentaTop(i,j);
                    else if(tmp==2) pentaLeft(i,j);
                    else if(tmp==3) pentaRight(i,j);
                }
                }
        }}
        updateMap();
    }

    public void addNewSeeds(int newSeeds){
        Random rng = new Random();
        int x,y;
        for(int i=0;i<newSeeds;i++){
            x=rng.nextInt((width-1)-1);
            y=rng.nextInt((height-1)-1);

                map[y][x].setState(true);
                newmap[y][x].setState(true);
                map[y][x].setColor(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
                newmap[y][x].setColor(map[y][x].getColor());



        }
    }

    private int energyCounter(int i, int j){
        int tmp=0;

        if(!(map[i-1][j-1].getColor().equals(map[i][j].getColor()))) tmp++;
        if(!(map[i-1][j].getColor().equals(map[i][j].getColor()))) tmp++;
        if(!(map[i-1][j+1].getColor().equals(map[i][j].getColor()))) tmp++;

        if(!(map[i][j-1].getColor().equals(map[i][j].getColor()))) tmp++;
        if(!(map[i][j+1].getColor().equals(map[i][j].getColor()))) tmp++;

        if(!(map[i+1][j-1].getColor().equals(map[i][j].getColor()))) tmp++;
        if(!(map[i+1][j].getColor().equals(map[i][j].getColor()))) tmp++;
        if(!(map[i+1][j+1].getColor().equals(map[i][j].getColor()))) tmp++;


        return tmp;
    }
    private int energyCounter2(int i, int j){
        int tmp=0;

        if(!(newmap[i-1][j-1].getColor().equals(newmap[i][j].getColor()))) tmp++;
        if(!(newmap[i-1][j].getColor().equals(newmap[i][j].getColor()))) tmp++;
        if(!(newmap[i-1][j+1].getColor().equals(newmap[i][j].getColor()))) tmp++;

        if(!(newmap[i][j-1].getColor().equals(newmap[i][j].getColor()))) tmp++;
        if(!(newmap[i][j+1].getColor().equals(newmap[i][j].getColor()))) tmp++;

        if(!(newmap[i+1][j-1].getColor().equals(newmap[i][j].getColor()))) tmp++;
        if(!(newmap[i+1][j].getColor().equals(newmap[i][j].getColor()))) tmp++;
        if(!(newmap[i+1][j+1].getColor().equals(newmap[i][j].getColor()))) tmp++;


        return tmp;
    }

    public void mcNext(){
        //lista wszystkich elementow
        allSeeds=new ArrayList<>();
        for (int i = 1; i < (height - 1); i++) {
            for (int j = 1; j < (width - 1); j++) {
                allSeeds.add(new CellIndex(j,i));
            }
        }

        //lista wszystkich stanow
        ArrayList<Color> states=new ArrayList<>();
        for (int i = 1; i < (height - 1); i++) {
            for (int j = 1; j < (width - 1); j++) {
                Color c=map[i][j].getColor();
                if(!states.contains(c))
                    states.add(c);
            }
        }

        //random
        Random rng=new Random();

        //losowanie elementow
        while(!allSeeds.isEmpty()){
            int tmpIndex=rng.nextInt(allSeeds.size()-0);
            int tmpX=allSeeds.get(tmpIndex).getX();
            int tmpY=allSeeds.get(tmpIndex).getY();
            allSeeds.remove(tmpIndex);


            int energy0=energyCounter2(tmpY,tmpX);

            Color c=map[tmpY][tmpX].getColor();
            Color c2=states.get(rng.nextInt(states.size()-0));

            //wez inny kolor z listy
            newmap[tmpY][tmpX].setColor(c2);
            int energy1=energyCounter2(tmpY,tmpX);

            if(energy1>energy0)
                newmap[tmpY][tmpX].setColor(c);
            else
                newmap[tmpY][tmpX].setColor(c2);
        }
    }

    private boolean randHex(){
        Random rng=new Random();
        boolean a=rng.nextBoolean();
        return a;
    }

    private int randPenta(){
        Random rng=new Random();
        int a=rng.nextInt(3-0);
        return a;
    }

    public void setNeigh(int neigh) {
        this.neigh = neigh;

        if(neigh==5 || neigh==4) {
            Random rng=new Random();
            choice=rng.nextBoolean();
        }
    }

    public void setPeriodic(boolean periodic) {
        this.periodic = periodic;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public void setSeedRule(int seedRule){
        this.seedRule = seedRule;
    }

    private boolean mooreRecrystal(int i, int j){
        int tmp=0;
        boolean tmp2=false;
        if(map[i-1][j-1].getColor().equals(map[i][j].getColor())) tmp++;
        if(map[i-1][j].getColor().equals(map[i][j].getColor())) tmp++;
        if(map[i-1][j+1].getColor().equals(map[i][j].getColor())) tmp++;

        if(map[i][j-1].getColor().equals(map[i][j].getColor())) tmp++;
        if(map[i][j+1].getColor().equals(map[i][j].getColor())) tmp++;

        if(map[i+1][j-1].getColor().equals(map[i][j].getColor())) tmp++;
        if(map[i+1][j].getColor().equals(map[i][j].getColor())) tmp++;
        if(map[i+1][j+1].getColor().equals(map[i][j].getColor())) tmp++;
        if(tmp!=8)
            tmp2=true;
    return tmp2;
    }

    private void recrystalization(){
        //stale
        int x=width-2;
        int y=height-2;
        double A=86710969050178.5;
        double B=9.41268203527779;
        // final double k=1000;
        //liicznie ro dla wszystkich krokow czasowych
        ArrayList<Double> roData=new ArrayList<>();
        for(int i=0;i<200;i++){
            Double tmp=A/B+(1-A/B)*exp(-B*i*0.001);
            roData.add(tmp);
        }

        //wartosc krytyczna
        ///wywalic to 100
        Double critRo=roData.get(65)/(x*y);








        //indexy komorek na granicy ziaren i wewnatrz
//        ArrayList<CellIndex> outsideSeeds=new ArrayList<>();
//        ArrayList<CellIndex> insideSeeds=new ArrayList<>();
//         for(int i=1;i<(height-1);i++){
//             for(int j=1;j<(width-1);j++){
//               CellIndex cellIndex=new CellIndex(i,j);
//               //if(randHex()){hexaLeft2(i,j);
//               if(mooreRecrystal(i,j)) insideSeeds.add(cellIndex);
//               else
//                   outsideSeeds.add(cellIndex);
//             }
//         }
//
        if(!lollol) {
            outsideSeeds = new ArrayList<>();
            insideSeeds = new ArrayList<>();
            for (int i = 1; i < (height - 1); i++) {
                for (int j = 1; j < (width - 1); j++) {
                    CellIndex cellIndex = new CellIndex(i, j);
                    //if(randHex()){hexaLeft2(i,j);
                    if (mooreRecrystal(i, j)) insideSeeds.add(cellIndex);
                    else
                        outsideSeeds.add(cellIndex);
                }
            }
            lollol = true;
        }



        recrystalCounter++;
      //  if(recrystalCounter==200) return;
         //delta ro
       if(recrystalCounter<200){
        roAll=0;
        for(int i=1;i<(height-1);i++){
            for(int j=1;j<(width-1);j++) {
                    roAll+=map[i][j].getRo();
            }
            }
        double delta=roData.get(recrystalCounter)-roAll;

        //update na kolejny steep

        //roAll+=delta;

        //ro na komorke
        double roCell=delta/(x*y);

        //przydzial
        for(CellIndex w:outsideSeeds) {
            newmap[w.getX()][w.getY()].setRo(newmap[w.getX()][w.getY()].getRo() + 0.2 * roCell);
//            newmap[w.getY()][w.getX()].setRo(newmap[w.getY()][w.getX()].getRo() + 0.2 * roCell);
            delta-=0.2*roCell;
        }
        for(CellIndex w:insideSeeds) {

            newmap[w.getX()][w.getY()].setRo(newmap[w.getX()][w.getY()].getRo() + 0.8 * roCell);
           // newmap[w.getY()][w.getX()].setRo(newmap[w.getY()][w.getX()].getRo() + 0.8 * roCell);
            delta -= 0.8 * roCell;
        }
        //System.out.println(roAll);
        //losowanie reszty
        Random rng=new Random();
        delta=delta/k;
        for(int i=0;i<k;i++){
            int index=rng.nextInt(insideSeeds.size()-0);
            int xx=insideSeeds.get(index).getX();
            int yy=insideSeeds.get(index).getY();
            newmap[xx][yy].setRo(newmap[xx][yy].getRo()+delta);
        }
//4,16 e8
//        double max=0.0;
//        for(int i=1;i<(height-1);i++){
//            for(int j=1;j<(width-1);j++) {
//                if(newmap[i][j].getRo()>max) { max=newmap[i][j].getRo();
//                }}}
//        System.out.println(max);
                    //sprawdz czy jakakolwiek komurka zrekrystalizowala i zmien jej stan
        for(int i=1;i<(height-1);i++){
            for(int j=1;j<(width-1);j++) {
                if((newmap[i][j].getRo()>=critRo) && (!newmap[i][j].isRecrystalized())){
                    newmap[i][j].setRecrystalized(true);
                    newmap[i][j].setRo(0.0);
                    newmap[i][j].setColor(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
                }
            }}

                //System.out.println(recrystalCounter+"   "+roAll);
        /*
        skonczyc loosowanie reszty
        parametr k jako text box w gui
        ustawienia do innej funkcji po sa robione raz a recrystal wywoluje sie w kazdym stepie
        update map zeby sie wyswietlalo
        losowanie nowego koloru i zerowanie syslokacjii. czy zerowanie nie wplynie na arraylisty?
        zacznij od cout ilosci syslokacji czy to co jest w ogole dziala
         dodaj if jak przekroczy crit value to rekrystalizacja  nowe ziarno z innymmi stanem pochalnia inne
         */
    }}

    private void mc(){

            //losowanie n stanow
            mcStates=new ArrayList<>();
            Random rng=new Random();
            for(int i=0;i<numberOfStates;i++){
                mcStates.add(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
            }

            for(int i=1;i<height-1;i++){
                for(int j=1;j<width-1;j++){
                    //map[i][j]=newmap[i][j];

                    newmap[i][j].setState(true);
                    newmap[i][j].setColor(mcStates.get(rng.nextInt(mcStates.size()-0)));

                }
            }



        }


    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isRecrystalization() {
        return recrystalization;
    }

    public void setRecrystalization(boolean recrystalization) {
        this.recrystalization = recrystalization;
    }

    public double getK() {
        return k;
    }

    public  void setK(double k) {
        Logic.k = k;
    }
}