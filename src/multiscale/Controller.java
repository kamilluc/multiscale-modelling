package multiscale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private int width,height;
    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private Canvas canvas;

    private GraphicsContext graphicsContext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void onButtonClicked(ActionEvent e){
        System.out.println("button clicked " +widthField.getText()+" "+Integer.parseInt(heightField.getText()));
        widthField.setDisable(true);
        heightField.setDisable(true);
        width=Integer.parseInt(widthField.getText());
        height=Integer.parseInt(heightField.getText());
        canvas=new Canvas(width,height);
        graphicsContext.clearRect(0, 0, width, height);

        graphicsContext.fillRect(5,5,10,10);
    }
}
