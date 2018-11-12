package multiscale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import multiscale.logic.CellularAutomata;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Controller implements Initializable {
    private int width,height,seeds;
    CellularAutomata ca;
    ObservableList list= FXCollections.observableArrayList();
    ObservableList structureList= FXCollections.observableArrayList();
    List<Color> selectedGrains;
    @FXML
    private ChoiceBox<String> series;
    @FXML
    private ChoiceBox<String> structureSeries;
    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField seedsField;

    @FXML
    private TextField numOfInclusions;

    @FXML
    private TextField sizeOfInclusions;

    @FXML
    private Canvas canvas;

    @FXML
    private Label selectedGrainsLabel;

    @FXML
    private CheckBox extendedMethod;

    @FXML
    private TextField probablity4thRule;
    private GraphicsContext graphicsContext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = canvas.getGraphicsContext2D();
        loadData();
        selectedGrains=new ArrayList();
        selectedGrainsLabel.setText("Selected Grains: 0");

    }

    private void loadData(){
        list.removeAll(list);
//        String a="Disable";
        list.addAll("Disable", "Square Random", "Square Boundaries", "Circle Random", "Circle Boundaries");
        series.getItems().addAll(list);

        structureList.addAll("Disable", "Substructure", "Dual-Phase");
        structureSeries.getItems().addAll(structureList);
    }

    public void onSetClicked(ActionEvent e){
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
        //todo: add rest
    widthField.setDisable(false);
    heightField.setDisable(false);
    seedsField.setDisable(false);
}
    public void onSteepClicked(ActionEvent e) {
        //todo: non blocking ui
        //fixme: bigger size doesnt work
        //int iter=0;
if(extendedMethod.isSelected()) {
    System.out.println("Extended Moore");
    ca.extendedMoore = true;
    ca.probablity4thRule=Integer.parseInt(probablity4thRule.getText());
}
else {
    ca.extendedMoore=false;
    //ca.probablity4thRule=Integer.parseInt(probablity4thRule.getText());

}
        System.out.println("Computing");

        while (!ca.isBoardFull())
        {

            ca.nextSteep();

            //iter++;
            //System.out.println(iter);
        }
        System.out.println("Adding Inclusions");
        addInclusion();
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
            //todo: copy this
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import from bitmap");
        fileChooser.setInitialDirectory(new File("microstructures/"));
//        fileChooser.setInitialFileName("grains.bmp");

        File file = fileChooser.showSaveDialog(new Stage());

        BufferedImage img = null;

        try {
            img=ImageIO.read(file);
        } catch (Exception s) {
        }

//        Image img0 = new Image(file);
//        Image image=new Image(file.getCanonicalPath());
        Image image = SwingFXUtils.toFXImage(img, null);

        graphicsContext.getCanvas().getGraphicsContext2D().drawImage(image,0,0);
//
//        WritableImage snap = graphicsContext.getCanvas().snapshot(null, null);
//image.getPixelReader().getColor(i,j);
        ca=new CellularAutomata(width,height);
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                ca.cellsOld[i+1][j+1].setState(image.getPixelReader().getColor(i,j));
                ca.cells[i+1][j+1].setState(image.getPixelReader().getColor(i,j));

            }
        }
        redrawCells();
//        graphicsContext.getCanvas().getGraphicsContext2D().drawImage(img.,0,0);

//        graphicsContext.getCanvas().getGraphicsContext2D().setFill(Color.ORANGE);
//               graphicsContext.getCanvas().getGraphicsContext2D().fillRect(0,0,300,300);

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
//        System.out.println(series.getValue());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import from txt");
        fileChooser.setInitialDirectory(new File("microstructures/"));
//        fileChooser.setInitialFileName("grains.bmp");

        File file = fileChooser.showSaveDialog(new Stage());

//        BufferedImage img = null;



//        Image img0 = new Image(file);
//        Image image=new Image(file.getCanonicalPath());
//        Image image = SwingFXUtils.toFXImage(img, null);

//        graphicsContext.getCanvas().getGraphicsContext2D().drawImage(image,0,0);
//
//        WritableImage snap = graphicsContext.getCanvas().snapshot(null, null);
//image.getPixelReader().getColor(i,j);

        try {
            List<String> lines = new ArrayList<>();
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(file));

                String st;
                while ((st = br.readLine()) != null){
                    lines.add(st);
                }
            }

            catch (IOException e)
            {

                // do something
                e.printStackTrace();
            }

            ca=new CellularAutomata(Integer.parseInt(lines.get(0)),Integer.parseInt(lines.get(1)));
int index=0;
            for(int i=0;i<Integer.parseInt(lines.get(0));i++){
                for(int j=0;j<Integer.parseInt(lines.get(1));j++){

index+=2;
                    ca.cellsOld[i+1][j+1].setState(Color.valueOf( lines.get(index)));
                    ca.cellsOld[i+1][j+1].setPhase(Integer.parseInt( lines.get(index+1)));
                    ca.cells[i+1][j+1].setState(Color.valueOf( lines.get(index)));
                    ca.cells[i+1][j+1].setPhase(Integer.parseInt( lines.get(index+1)));



                }
            }
        } catch (Exception s) {
        }



        redrawCells();
    }

    private void redrawCells(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graphicsContext.setFill(ca.cellsOld[i + 1][j + 1].getState());
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }

    @FXML
    private void addInclusion(){
        //todo: dynamic z gui -- 2 typy i po 2 rodzaje na granicy i andom -- na granicy moga byc dodane tylko na koncu obliczen, random lepiej na poczatku
        //todo it should be in logic

//        String inclusionType="Square";
//        int inclusionNumber=6;
//        double inclusionSize=12.;
//
//        if(inclusionType.equalsIgnoreCase("squarerandom")){
//            int a= (int) Math.floor(inclusionSize/Math.sqrt(2));
//            Random rng=new Random();
//            int x=rng.nextInt((width-1))+1;
//            int y=rng.nextInt((height-1))+1;
//            //todo: check zakres
//            for(int i=0;i<a;i++){
//                for(int j=0;j<a;j++){
//                    if(x+i>width-1 || y+j>height-1 || x+i==0 || y+j==0)
//                        continue;
//                    ca.cells[x+i][y+j].setState(Color.BLACK);
//                }
//            }
//        }
//        else if(inclusionType.equalsIgnoreCase("circlerandom")){
//
//        }

        ca.addInclusions(series.getValue(), Integer.parseInt(numOfInclusions.getText()), Integer.parseInt(sizeOfInclusions.getText()));
        redrawCells();
    }

    @FXML
    private void addGrainToList(MouseEvent mouseEvent){
        if(mouseEvent.isShiftDown()){
            if (selectedGrains.contains(ca.cells[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].getState()))
                selectedGrains.remove(ca.cells[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].getState());
        }

        else {
            if (!selectedGrains.contains(ca.cells[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].getState()))
                selectedGrains.add(ca.cells[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].getState());
        }
        selectedGrainsLabel.setText("Selected Grains: "+selectedGrains.size());

    }

    @FXML
    private void clearNonSelectedGrains(){
        ca.removeNonSelectedGrains(selectedGrains);
        System.out.println("Grains removed");
        redrawCells();
    }

    @FXML
    private void continueAfterRemove(){
        seeds=Integer.parseInt(seedsField.getText());

        ca.seedGrains(seeds);
        selectedGrains.clear();
        selectedGrainsLabel.setText("Selected Grains: "+selectedGrains.size());

        redrawCells();
    }
    }


