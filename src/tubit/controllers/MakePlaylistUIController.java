/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.controllers;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import tubit.models.MakePlaylistModel;
import tubit.models.MakePlaylistModel.SEARCH_CRITERIA;
import tubit.models.Song;

/**
 * FXML Controller class
 *
 * @author Ofir
 */
public class MakePlaylistUIController extends TubitBaseController {

    // inner class for simple fill to tables after query is executed in model.
    /**private class DisplayedSong {

        public SimpleStringProperty songName;
        public SimpleStringProperty singerName;
        public SimpleStringProperty albumName;
        public SimpleIntegerProperty duration;
        public SimpleIntegerProperty year;

        public DisplayedSong(String songName, String singerName, String albumName, Integer duration, Integer year) {
            this.songName = new SimpleStringProperty(songName);
            this.singerName = new SimpleStringProperty(singerName);
            this.albumName = new SimpleStringProperty(albumName);
            this.duration = new SimpleIntegerProperty(duration);
            this.year = new SimpleIntegerProperty(year);
        }
    }**/
    @FXML
    ScrollPane scroll_searchResults;
    @FXML
    ScrollPane scroll_chosenSongs;
    @FXML
    JFXTextField searchField;
    @FXML
    ToggleGroup songSearchingSubject;

    @FXML
    public TableView<Song> queriedSongs;
    @FXML
    public TableColumn<Song, String> songName;
    @FXML
    public TableColumn<Song, String> singerName;
    @FXML
    public TableColumn<Song, String> albumName;
    @FXML
    public TableColumn<Song, Integer> duration;
    @FXML
    public TableColumn<Song, Integer> year;

    MakePlaylistModel model;
    List<Song> c_queriedSongs;
    ObservableList<Song> c_displayed_queries;
    List<Song> chosenSongs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new MakePlaylistModel();
        chosenSongs = new ArrayList<>();

        songName.setCellValueFactory(new PropertyValueFactory<>("songName"));
        singerName.setCellValueFactory(new PropertyValueFactory<>("SingerName"));
        albumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));

        //queriedSongs.setItems(c_displayed_queries);
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
        c_queriedSongs = model.extractSongs(getSearchCriteria(), searchField.getText());
        c_displayed_queries = FXCollections.observableArrayList(c_queriedSongs);
        queriedSongs.getItems().setAll(c_displayed_queries);
    }

    @FXML
    private void uploadPhoto(MouseEvent event) throws IOException {
    }

    @FXML
    private void uploadPlaylistToDB(MouseEvent event) throws IOException {

    }

    private SEARCH_CRITERIA getSearchCriteria() {
        RadioButton selectedRadioButton = (RadioButton) songSearchingSubject.getSelectedToggle();
        String filter = selectedRadioButton.getText();
        switch (filter) {
            case "Song name":
                return SEARCH_CRITERIA.SONG_NAME;
            case "Singer name":
                return SEARCH_CRITERIA.SINGER_NANE;
            case "Album name":
                return SEARCH_CRITERIA.ALBUM_NAME;
        }
        // unreachable code.
        return null;
    }
}
