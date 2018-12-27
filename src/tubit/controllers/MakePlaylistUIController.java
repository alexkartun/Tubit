/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import tubit.controllers.TubitBaseController;

/**
 * <fx:define>
 * <ToggleGroup fx:id="songSearchingSubject"/>
 * </fx:define>
 */
/**
 * FXML Controller class
 *
 * @author Ofir
 */
public class MakePlaylistUIController extends TubitBaseController {

    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void backToPlaylistChooser(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/PlaylistChooserUI.fxml");
    }

    @FXML
    private void addSongToPlaylist(MouseEvent event) throws IOException {
    }

    @FXML
    private void extractSongs(MouseEvent event) throws IOException {
    }

    @FXML
    private void uploadPhoto(MouseEvent event) throws IOException {
    }

    @FXML
    private void uploadPlaylistToDB(MouseEvent event) throws IOException {

    }
}
