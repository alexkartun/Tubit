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
import javafx.stage.Stage;
import tubit.models.ClientData;

public class MainUIController extends TubitBaseController {

    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXPasswordField passwordInput;
    
    static ClientData client;

    @FXML
    private void openRegistration(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/RegistrationUI.fxml");
    }

    @FXML
    private void loginAction(MouseEvent event) throws IOException {
        String username = this.usernameInput.getText();
        String password = this.passwordInput.getText();
        if (validateInputs(username, password)) {
            client = DBUtils.getInstance().checkClientInDB(username, password);
            if (client.status) {
                System.out.println("Connected");
                refreshPage("/tubit/views/PlaylistChooserUI.fxml");
            } else {
                 System.out.println("Not");
            }
        }
    }

    private boolean validateInputs(String username, String password) {
        boolean correctInputs = true;
        if (username.isEmpty()) {
            correctInputs = false;
            this.usernameInput.getStylesheets().clear();
            this.usernameInput.getStylesheets().add("/resources/css/error.css");
        } else {
            this.usernameInput.getStylesheets().clear();
            this.usernameInput.getStylesheets().add("/resources/css/stylesheet.css");
        }
        if (password.isEmpty()) {
            correctInputs = false;
            this.passwordInput.getStylesheets().clear();
            this.passwordInput.getStylesheets().add("/resources/css/error.css");
        } else {
            this.passwordInput.getStylesheets().clear();
            this.passwordInput.getStylesheets().add("/resources/css/stylesheet.css");
        }
        return correctInputs;
    }
}
