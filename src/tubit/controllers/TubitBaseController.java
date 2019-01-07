
package tubit.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 *  This class is a base class for all the controllers.
 *  Implements Initializable.
 */
public class TubitBaseController implements Initializable {
    protected double xOffset = 0;
    protected double yOffset = 0;
    @FXML
    protected AnchorPane rootPane;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDraggable();
    }
    /**
     * This function let the window be draggable.
     */
    protected void makeStageDraggable() {
        rootPane.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        rootPane.setOnMouseDragged((MouseEvent event) -> {
            rootPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            rootPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }
    /**
     * This function close the stage by mouse click event.
     * 
     * @param event - (MouseEvent) 
     */
    @FXML
    protected void closeApp(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    /**
     * This function refresh the AncorPane between to screens.
     * 
     * @param resourceFX - (String) new screen info.
     * 
     * @throws IOException 
     */
    protected void refreshPage(String resourceFX) throws IOException {
        AnchorPane pane = (AnchorPane) FXMLLoader.load(getClass().getResource(resourceFX));
        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(pane);
    }
}
