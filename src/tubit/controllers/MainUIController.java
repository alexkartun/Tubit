package tubit.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBUtils;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import tubit.models.ClientData;
/**
 * This class is the first controller.
 * Extends TubitBaseController
 */
public class MainUIController extends TubitBaseController {

    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXPasswordField passwordInput;
    
    static ClientData client;
    /**
     * This function open the RegistrationUI.fxml
     * 
     * @param event - (MouseEvent) mouse click event
     *
     * @throws IOException 
     */
    @FXML
    private void openRegistration(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/RegistrationUI.fxml");
    }
    /**
     * This function handle the login action.
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void loginAction(MouseEvent event) throws IOException {
        String username = this.usernameInput.getText();
        String password = this.passwordInput.getText();
        if (validateInputs(username, password)) {
            client = DBUtils.getInstance().checkClientInDB(username, password);
            if (client.status) {
                refreshPage("/tubit/views/PlaylistChooserUI.fxml");
            }
        }
    }
    /**
     * This function check if the inputs are valid.
     * 
     * @param username - (String) user name
     * @param password - (Sring) user password
     * 
     * @return true if valid, false otherwise.
     */
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
