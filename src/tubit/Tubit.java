package tubit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * Main class for Tubit project !!
 * Extends Application.
 */
public class Tubit extends Application {
    /**
     * This function start the app.
     * @param stage -(Stage) initialize stage
     * 
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/MainUI.fxml"));
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Main function.
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }

}
