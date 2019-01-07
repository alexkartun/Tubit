package tubit.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBUtils;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * This class is the registration controller.
 * Extends TubitBaseController.
 *
 */
public class RegistrationUIController extends TubitBaseController {
    
    
    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXTextField emailnput;
    @FXML
    private JFXPasswordField passwordInput;
    @FXML
    private JFXPasswordField confirmPasswordInput;
    
    /**
     * This function get back the MainUI.fxml
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void backToMenu(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/MainUI.fxml");
    }
    /**
     * This function handle the registration action.
     * 
     * @param event - (MouseEvent) mouse click event.
     * 
     * @throws IOException 
     */
    @FXML
    private void registrationAction(MouseEvent event) throws IOException {
        String username = this.usernameInput.getText();
        String email = this.emailnput.getText();
        String password = this.passwordInput.getText();
        String confirmPassword = this.confirmPasswordInput.getText();
        if (validateInputs(username, email, password, confirmPassword)) {
            if (DBUtils.getInstance().updateDB(username, email, password)) {
                //TODO: Game menu
                System.out.println("Connected");
                refreshPage("/tubit/views/MainUI.fxml");
            } else {
                 System.out.println("Not");
            }
        }
    }
    /**
     * This function check if the inputs are valid.
     * 
     * @param username - (String) user name
     * @param email - (String) user email
     * @param password - (Sring) user password
     * @param confirmPassword - (Sring) user password
     *
     * @return true if valid, false otherwise.
     */
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
