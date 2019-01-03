/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.controllers;

import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import tubit.models.MakePlaylistModel;
import tubit.models.MakePlaylistModel.SEARCH_CRITERIA;
import tubit.models.Song;

/**
 * FXML Controller class
 *
 * @author Ofir
 */
public class MakePlaylistUIController extends TubitBaseController {
    @FXML
    ScrollPane scroll_searchResults;
    @FXML
    ScrollPane scroll_chosenSongs;
    @FXML
    JFXTextField searchField;
    @FXML
    JFXTextField playlistName;
    @FXML
    ToggleGroup songSearchingSubject;
    @FXML
    Button removeChosen;
    @FXML
    ImageView playlistPhoto;

    @FXML
    public TableView<Song> queriedSongsTable;
    @FXML
    public TableColumn<Song, String> q_songName;
    @FXML
    public TableColumn<Song, String> q_singerName;
    @FXML
    public TableColumn<Song, String> q_albumName;
    @FXML
    public TableColumn<Song, Integer> q_duration;
    @FXML
    public TableColumn<Song, Integer> q_year;
    
    @FXML
    public TableView<Song> playlistSongsTable;
    @FXML
    public TableColumn<Song, String> p_songName;
    @FXML
    public TableColumn<Song, String> p_singerName;
    @FXML
    public TableColumn<Song, String> p_albumName;
    @FXML
    public TableColumn<Song, Integer> p_duration;
    @FXML
    public TableColumn<Song, Integer> p_year;

    MakePlaylistModel model;
    List<Song> c_queriedSongs;
    List<Song> c_chosenSongs;
    ByteArrayInputStream picBlob;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new MakePlaylistModel();
        c_chosenSongs = new ArrayList<>();
        bindTablesColumns();
        playlistPhoto.setImage(new Image("file:\\\\\\" + "C:\\Users\\Ofir\\Documents\\NetBeansProjects\\Tubit\\src\\resources\\images\\question-mark.jpg"));
    }

    @FXML
    private void backToPlaylistChooser(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/PlaylistChooserUI.fxml");
    }

    @FXML
    private void addSongToPlaylist(MouseEvent event) throws IOException {
        Song chosenSong = queriedSongsTable.getSelectionModel().getSelectedItem();
        if (chosenSong == null) { // button is pressed without selecting a song. do nothing.
            return;
        }
        c_chosenSongs.add(chosenSong);
        c_queriedSongs.remove(chosenSong);
        queriedSongsTable.setItems(FXCollections.observableArrayList(c_queriedSongs));
        playlistSongsTable.setItems(FXCollections.observableArrayList(c_chosenSongs));
    }

    @FXML
    private void extractSongs(MouseEvent event) throws IOException {
        c_queriedSongs = model.extractSongs(getSearchCriteria(), searchField.getText());
        removeAlreadyChosenSongs();
        queriedSongsTable.setItems(FXCollections.observableArrayList(c_queriedSongs));
    }

    @FXML
    private void uploadPhoto(MouseEvent event) throws IOException {
        JFileChooser picChooser = new JFileChooser("C:\\Users\\Ofir\\Documents\\NetBeansProjects\\Tubit");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG photos only", "png");
        picChooser.setFileFilter(filter);
        picChooser.setApproveButtonText("Choose playlist photo");
        int returnVal = picChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String imagePath = picChooser.getSelectedFile().getPath();
            //Image image = SwingFXUtils.toFXImage(ImageIO.read(file), null); // convert file to image
            Image fixedSizeImage =  new Image("file:\\\\\\" + imagePath, 100.0, 100.0, false, false);
            playlistPhoto.setImage(fixedSizeImage);
            picBlob = model.getBlob(fixedSizeImage);
        }
    }
    
    @FXML
    private void removeChosen(MouseEvent event) throws IOException {
        Song removedSong = playlistSongsTable.getSelectionModel().getSelectedItem();
        c_chosenSongs.remove(removedSong);
        playlistSongsTable.setItems(FXCollections.observableArrayList(c_chosenSongs));
        removeChosen.setDisable(true);
    }
    @FXML
    private void enableRemoveButton(MouseEvent event) throws IOException {
        Song chosenSong = playlistSongsTable.getSelectionModel().getSelectedItem();
        if (chosenSong != null) {
            removeChosen.setDisable(false);
        }
    }
    
    @FXML
    private void createPlaylist(MouseEvent event) throws IOException {
        model.uploadPlaylistToDB(playlistName.getText(), picBlob, c_chosenSongs);
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
    
    private void bindTablesColumns() {
        q_songName.setCellValueFactory(new PropertyValueFactory<>("songName"));
        q_singerName.setCellValueFactory(new PropertyValueFactory<>("singerName"));
        q_albumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));
        q_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        q_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        p_songName.setCellValueFactory(new PropertyValueFactory<>("songName"));
        p_singerName.setCellValueFactory(new PropertyValueFactory<>("singerName"));
        p_albumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));
        p_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        p_year.setCellValueFactory(new PropertyValueFactory<>("year"));
    }
    
    private void removeAlreadyChosenSongs() {
        List<Song> toBeRemoved = new ArrayList<>();
        // iterate through queried list without modifing it on the same time.
        for (Song s : c_queriedSongs) {
            for (Song chosen : c_chosenSongs) {
                if (s.getSongId() == chosen.getSongId()) {
                    toBeRemoved.add(s);
                    break;
                }
            }
        }
        c_queriedSongs.removeAll(toBeRemoved);
    }
        

}
