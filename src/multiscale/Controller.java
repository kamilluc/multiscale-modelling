package multiscale;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import multiscale.logic.CellularAutomata;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private int width,height,seeds;
    CellularAutomata ca;

    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField seedsField;

    @FXML
    private Canvas canvas;

    private GraphicsContext graphicsContext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void onButtonClicked(ActionEvent e){
//        System.out.println("button clicked " +widthField.getText()+" "+Integer.parseInt(heightField.getText()));
        widthField.setDisable(true);
        heightField.setDisable(true);
        seedsField.setDisable(true);
        width=Integer.parseInt(widthField.getText());
        height=Integer.parseInt(heightField.getText());
        seeds=Integer.parseInt(seedsField.getText());
        canvas=new Canvas(width,height);
        graphicsContext.clearRect(0, 0, width, height);
        ca=new CellularAutomata(width,height);
        ca.seedGrains(seeds);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graphicsContext.setFill(ca.cells[i + 1][j + 1].getState());
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }
    private void unlockInterface(){
    widthField.setDisable(false);
    heightField.setDisable(false);
    seedsField.setDisable(false);
}
    public void onButtonSteep(ActionEvent e) {
        //todo: non blocking ui
        //fixme: bigger size doesnt work
        //int iter=0;

        System.out.println("Computing");
        while (!ca.isBoardFull())
        {

            ca.nextSteep();

            //iter++;
            //System.out.println(iter);
        }
        System.out.println("Drawing");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graphicsContext.setFill(ca.cells[i + 1][j + 1].getState());
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
        System.out.println("Done");
        unlockInterface();
    }

    @FXML
    public void exportToBitmap() throws Exception {

//        WritableImage exportedImage = new WritableImage(width, height);
//        canvas.snapshot(null, exportedImage);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to bitmap");
        fileChooser.setInitialDirectory(new File("microstructures/"));
        fileChooser.setInitialFileName("grains.bmp");

        File file = fileChooser.showSaveDialog(new Stage());



        WritableImage wim = new WritableImage(height, width);
        graphicsContext.getCanvas().snapshot(null, wim);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (Exception s) {
        }

    }

    @FXML
    public void importFromBitmap() throws Exception {

    }

    @FXML
    public void exportToTxt() throws Exception {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export to text file");
        chooser.setInitialDirectory(new File("microstructures/"));
        chooser.setInitialFileName("grains.txt");

        File file = chooser.showSaveDialog(new Stage());

        if (file != null) {


            List<String> data = new ArrayList<>();
data.add(String.valueOf(width)+"\n");
            data.add(String.valueOf(height)+"\n");
            for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    data.add(ca.cells[i][j].getState()+"\n"+ca.cells[i][j].getPhase()+"\n");
                }
            }

            FileWriter writer = new FileWriter(file);



            for (String item : data) {
                writer.write(item);

            }

            writer.close();
        }
    }

    @FXML
    public void importFromTxt() throws Exception {

    }


    @FXML
    public void addInclusion(){
        //todo: dynamic z gui -- 2 typy i po 2 rodzaje na granicy i andom -- na granicy moga byc dodane tylko na koncu obliczen, random lepiej na poczatku
        //todo it should be in logic

        String inclusionType="Square";
        int inclusionNumber=6;
        double inclusionSize=12.;

        if(inclusionType.equalsIgnoreCase("squarerandom")){
            int a= (int) Math.floor(inclusionSize/Math.sqrt(2));
            Random rng=new Random();
            int x=rng.nextInt((width-1))+1;
            int y=rng.nextInt((height-1))+1;
            //todo: check zakres
            for(int i=0;i<a;i++){
                for(int j=0;j<a;j++){
                    if(x+i>width-1 || y+j>height-1 || x+i==0 || y+j==0)
                        continue;
                    ca.cells[x+i][y+j].setState(Color.BLACK);
                }
            }
        }
        else if(inclusionType.equalsIgnoreCase("circlerandom")){

        }
    }
    }


