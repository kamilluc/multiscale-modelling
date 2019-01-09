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
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import multiscale.logic.CellularAutomata;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    private int width, height, seeds;
    CellularAutomata ca;
    ObservableList list = FXCollections.observableArrayList();
    ObservableList structureList = FXCollections.observableArrayList();
    ObservableList energyDistList = FXCollections.observableArrayList();
    ObservableList nucleationTypeList = FXCollections.observableArrayList();
    List<Color> selectedGrains;

    @FXML
    private ChoiceBox<String> series;

    @FXML
    private ChoiceBox<String> structureSeries;
    @FXML
    private ChoiceBox<String> energyDistSeries;
    @FXML


    private TextField widthField;

    @FXML
    private ChoiceBox<String> nucleationTypeSeries;
    @FXML
    private TextField iterationsMc;
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
    private TextField hminText;
    @FXML
    private TextField hmaxText;

    @FXML
    private TextField recrystallIterationsText;

    @FXML
    private TextField recrystallNucleonsText;
    @FXML
    private TextField probablity4thRule;

    private GraphicsContext graphicsContext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = canvas.getGraphicsContext2D();
        loadData();
        selectedGrains = new ArrayList();
        selectedGrainsLabel.setText("Selected Grains: 0");
        series.setValue("Disable");
        structureSeries.setValue("Disable");
        energyDistSeries.setValue("Disable");
        nucleationTypeSeries.setValue("Disable");
    }

    private void loadData() {
        list.removeAll(list);
        list.addAll("Disable", "Square Random", "Square Boundaries", "Circle Random", "Circle Boundaries");
        series.getItems().addAll(list);
        structureList.addAll("Disable", "Substructure", "Dual-Phase");
        structureSeries.getItems().addAll(structureList);

        energyDistList.addAll("Disable", "Homogeneous", "Heterogeneus");
        energyDistSeries.getItems().addAll(energyDistList);

        nucleationTypeList.addAll("Disabl", "Constant", "Increasing");
        nucleationTypeSeries.getItems().addAll(nucleationTypeList);
    }

    public void onSetClicked(ActionEvent e) {
        //  widthField.setDisable(true);
        // heightField.setDisable(true);
        //  seedsField.setDisable(true);
        width = Integer.parseInt(widthField.getText());
        height = Integer.parseInt(heightField.getText());
        seeds = Integer.parseInt(seedsField.getText());
        canvas = new Canvas(width, height);
        graphicsContext.clearRect(0, 0, width, height);
        ca = new CellularAutomata(width, height);
        ca.seedGrains(seeds);
        //todo: all of it should be in separate function and call it from here and after other buttons clicks
        ca.probablity4thRule = Integer.parseInt(probablity4thRule.getText());
        ca.extendedMoore = extendedMethod.isSelected();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graphicsContext.setFill(ca.cells[i + 1][j + 1].getState());
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }

    private void unlockInterface() {
        //todo: add rest gui elements
        widthField.setDisable(false);
        heightField.setDisable(false);
        seedsField.setDisable(false);
    }


    public void onSteepClicked(ActionEvent e) {
        //todo: non blocking ui -> separate thread for gui
        //fixme: bigger size doesnt work properly -> gui

        if (extendedMethod.isSelected()) {
            System.out.println("Extended Moore");
            ca.extendedMoore = true;
            ca.probablity4thRule = Integer.parseInt(probablity4thRule.getText());
        } else {
            ca.extendedMoore = false;
        }
       // ca.clearBorders();
        System.out.println("Computing");

        System.out.println("Drawing");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graphicsContext.setFill(ca.cells[i + 1][j + 1].getState());
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }

        int iter = 0;
        while (!ca.isBoardFull()) {
            iter++;
            if (iter > 1000) break;
            ca.nextSteep();
        }


        System.out.println("Drawing");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graphicsContext.setFill(ca.cells[i + 1][j + 1].getState());
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
        System.out.println("Done");
        // unlockInterface();
    }

    @FXML
    public void exportToBitmap() throws Exception {
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import from bitmap");
        fileChooser.setInitialDirectory(new File("microstructures/"));
        File file = fileChooser.showSaveDialog(new Stage());
        BufferedImage img = null;

        try {
            img = ImageIO.read(file);
        } catch (Exception s) {
        }

        Image image = SwingFXUtils.toFXImage(img, null);
        graphicsContext.getCanvas().getGraphicsContext2D().drawImage(image, 0, 0);
        ca = new CellularAutomata(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ca.cellsOld[i + 1][j + 1].setState(image.getPixelReader().getColor(i, j));
                ca.cells[i + 1][j + 1].setState(image.getPixelReader().getColor(i, j));

            }
        }
        redrawCells();
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
            data.add(String.valueOf(width) + "\n");
            data.add(String.valueOf(height) + "\n");
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    data.add(ca.cells[i][j].getState() + "\n" + ca.cells[i][j].getPhase() + "\n");
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import from txt");
        fileChooser.setInitialDirectory(new File("microstructures/"));
        File file = fileChooser.showSaveDialog(new Stage());

        try {
            List<String> lines = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    lines.add(st);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            ca = new CellularAutomata(Integer.parseInt(lines.get(0)), Integer.parseInt(lines.get(1)));
            int index = 0;

            for (int i = 0; i < Integer.parseInt(lines.get(0)); i++) {
                for (int j = 0; j < Integer.parseInt(lines.get(1)); j++) {
                    index += 2;
                    ca.cellsOld[i + 1][j + 1].setState(Color.valueOf(lines.get(index)));
                    ca.cellsOld[i + 1][j + 1].setPhase(Integer.parseInt(lines.get(index + 1)));
                    ca.cells[i + 1][j + 1].setState(Color.valueOf(lines.get(index)));
                    ca.cells[i + 1][j + 1].setPhase(Integer.parseInt(lines.get(index + 1)));
                }
            }

        } catch (Exception s) {
        }

        redrawCells();
    }

    private void redrawCells() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graphicsContext.setFill(ca.cellsOld[i + 1][j + 1].getState());
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }

    @FXML
    public void addInclusion() {
        ca.addInclusionsV2(series.getValue(), Integer.parseInt(numOfInclusions.getText()), Integer.parseInt(sizeOfInclusions.getText()));
        redrawCells();
    }

    @FXML
    private void addGrainToList(MouseEvent mouseEvent) {
        if (mouseEvent.isShiftDown()) {
            if (selectedGrains.contains(ca.cells[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].getState()))
                selectedGrains.remove(ca.cells[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].getState());
        } else {
            if (!selectedGrains.contains(ca.cells[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].getState()))
                selectedGrains.add(ca.cells[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].getState());
        }
        selectedGrainsLabel.setText("Selected Grains: " + selectedGrains.size());
    }

    @FXML
    private void clearNonSelectedGrains() {
        ca.removeNonSelectedGrains(selectedGrains, structureSeries.getValue());
        System.out.println("Grains removed");
        redrawCells();
    }

    @FXML
    private void continueAfterRemove() {
        seeds = Integer.parseInt(seedsField.getText());
        ca.seedGrains(seeds);
        selectedGrains.clear();
        selectedGrainsLabel.setText("Selected Grains: " + selectedGrains.size());
        redrawCells();
        ca.clearBorders();

    }

    @FXML
    private void onEntireClearClick() {
        ca.clearSpace();
        redrawCells();
    }

    @FXML
    private void onSelectedClearClick() {
        ca.clearSelectedSpace(selectedGrains);
        selectedGrains.clear();
        selectedGrainsLabel.setText("Selected Grains: " + selectedGrains.size());
        redrawCells();
    }

    @FXML
    private void onMonteCarloStartClick() {
//        System.out.println("mc");
        seeds = Integer.parseInt(seedsField.getText());
        ca.initMC(seeds);
        redrawCells();

    }

    @FXML
    private void onMonteCarloRunClick() {
//        System.out.println("mc2");
        int iteration = Integer.parseInt(iterationsMc.getText());
        for (int i = 0; i < iteration; i++) {
            ca.nextMCSteep();
//            System.out.println("MC iteration:\t" + (i+1));
        }
//        ca.clearBorders();
        //ca.updateCells();
        redrawCells();
    }

    private boolean mode = true;

    @FXML
    private void onChangeViewMode() {
        if (!mode) {
            redrawCells();
        } else {
//               int hmin=10000,hmax=-1;
//
//               for (int i = 0; i < width; i++) {
//                   for (int j = 0; j < height; j++) {
//                       int h=ca.cellsOld[i + 1][j + 1].getH();
//                       if(h>hmax)   hmax=h;
//                       if(h<hmin)   hmin=h;
//                   }
//               }


            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int h = ca.cellsOld[i + 1][j + 1].getH();
                    Color c;
                    int hmax = Integer.parseInt(hmaxText.getText());
                    int hmin = Integer.parseInt(hminText.getText());
                    if (h >= hmax) c = Color.GREEN;
                    else if (h == 0) c = Color.RED;
                    else c = Color.BLUE;


                    graphicsContext.setFill(c);
                    graphicsContext.fillRect(i, j, 1, 1);
                }
            }


        }
        mode = !mode;
    }

    @FXML
    private void startRecrystalization() {
        int hmin = Integer.parseInt(hminText.getText());
        int hmax = Integer.parseInt(hmaxText.getText());
        String energyDist = energyDistSeries.getValue();
        String nucleationType = nucleationTypeSeries.getValue();
        int numOfNucleons = Integer.parseInt(recrystallNucleonsText.getText());
        int iterations = Integer.parseInt(recrystallIterationsText.getText());

        //todo: add above elements to gui
        if (energyDist.equalsIgnoreCase("Heterogeneus")) {
            // sprawdz grain boundary jesli true energy = hmax
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int e=ca.calculateEnergy(i+1,j+i);
                    if (e>0){
                        //change on hmax
                        ca.cellsOld[i+1][j+1].setH(11);
                    }
                }
            }
        }
    }
}