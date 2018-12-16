package tubit.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegistrationUIController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXTextField emailnput;
    @FXML
    private JFXPasswordField passwordInput;
    @FXML
    private JFXPasswordField confirmPasswordInput;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeStageDraggable();
    }

    private void makeStageDraggable() {
        rootPane.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        rootPane.setOnMouseDragged((MouseEvent event) -> {
            rootPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            rootPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
            rootPane.getScene().setFill(Color.TRANSPARENT);
        });
    }

    @FXML
    private void backToMenu(MouseEvent event) throws IOException {
        AnchorPane pane = (AnchorPane) FXMLLoader.load(getClass().getResource("/tubit/views/MainUI.fxml"));
        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void closeApp(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void registrationAction(MouseEvent event) {
        String username = this.usernameInput.getText();
        String email = this.emailnput.getText();
        String password = this.passwordInput.getText();
        String confirmPassword = this.confirmPasswordInput.getText();
        if (validateInputs(username, email, password, confirmPassword)) {
            if (DBUtils.getInstance().updateDB(username, email, password)) {
                //TODO: Game menu
                System.out.println("Connected");
            } else {
                 System.out.println("Not");
            }
        }
    }

    private boolean validateInputs(String username, String email, String password, String confirmPassword) {
        boolean correctInputs = true;
        if (username.isEmpty()) {
            correctInputs = false;
            this.usernameInput.getStylesheets().clear();
            this.usernameInput.getStylesheets().add("/resources/css/error.css");
        } else {
            this.usernameInput.getStylesheets().clear();
            this.usernameInput.getStylesheets().add("/resources/css/stylesheet.css");
        }
        if (email.isEmpty()) {
            correctInputs = false;
            this.emailnput.getStylesheets().clear();
            this.emailnput.getStylesheets().add("/resources/css/error.css");
        } else {
            this.emailnput.getStylesheets().clear();
            this.emailnput.getStylesheets().add("/resources/css/stylesheet.css");
        }
        if (password.isEmpty()) {
            correctInputs = false;
            this.passwordInput.getStylesheets().clear();
            this.passwordInput.getStylesheets().add("/resources/css/error.css");
        } else {
            this.passwordInput.getStylesheets().clear();
            this.passwordInput.getStylesheets().add("/resources/css/stylesheet.css");
        }
        if (confirmPassword.isEmpty()) {
            correctInputs = false;
            this.confirmPasswordInput.getStylesheets().clear();
            this.confirmPasswordInput.getStylesheets().add("/resources/css/error.css");
        } else {
            this.confirmPasswordInput.getStylesheets().clear();
            this.confirmPasswordInput.getStylesheets().add("/resources/css/stylesheet.css");
        }
        if (!(password.isEmpty() || confirmPassword.isEmpty() || password.equals(confirmPassword))) {
            correctInputs = false;
            this.passwordInput.getStylesheets().clear();
            this.passwordInput.getStylesheets().add("/resources/css/error.css");
            this.confirmPasswordInput.getStylesheets().clear();
            this.confirmPasswordInput.getStylesheets().add("/resources/css/error.css");
        }
        return correctInputs;
    }
}
