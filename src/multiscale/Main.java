package multiscale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setTitle("Multiscale Modeling");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.getIcons().add(new Image("file:icon.png"));
        String css =this.getClass().getResource("Style.css").toExternalForm();
        root.getStylesheets().add(css);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
