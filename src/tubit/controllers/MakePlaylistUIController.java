package tubit.controllers;

import com.jfoenix.controls.JFXTextField;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import tubit.models.MakePlaylistModel;
import tubit.models.MakePlaylistModel.SEARCH_CRITERIA;
import tubit.models.Song;

/**
 * This class generate new playlist.
 * Extends TubitBaseController.
 * 
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
     * This function initializes the controller class.
     * 
     * @param url - (URL) 
     * @param rb - (ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new MakePlaylistModel();
        c_chosenSongs = new ArrayList<>();
        bindTablesColumns();
        Image defaultImage = new Image("/resources/images/question-mark.jpg");
        playlistPhoto.setImage(defaultImage);
        picBlob = model.getBlob(defaultImage);
    }
    /**
     * This function get back the PlaylistChooserUI.fxml
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void backToPlaylistChooser(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/PlaylistChooserUI.fxml");
    }
    /**
     * This function add new song into the playlist.
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
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
    /**
     * This function extract songs from playlist.
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void extractSongs(MouseEvent event) throws IOException {
        c_queriedSongs = model.extractSongs(getSearchCriteria(), capitalize(searchField.getText()));
        removeAlreadyChosenSongs();
        queriedSongsTable.setItems(FXCollections.observableArrayList(c_queriedSongs));
    }
    /**
     * This functon capitalize strings
     * 
     * @param str - (String) 
     * 
     * @return new string with capital letters.
     */
    public static String capitalize(String str) {
        String words[] = str.replaceAll("\\s+", " ").trim().split(" ");
        String newSentence = "";
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                newSentence = newSentence + ((i == 0) ? word.substring(i, i + 1).toUpperCase()
                        : (i != word.length() - 1) ? word.substring(i, i + 1).toLowerCase() : word.substring(i, i + 1).toLowerCase().toLowerCase() + " ");
            }
        }

        return newSentence.substring(0, newSentence.length() - 1);
    }
    /**
     * This function update the playlist image.
     * 
     * @param event - (MouseEvent) mouse click event
     *
     * @throws IOException 
     */
    @FXML
    private void uploadPhoto(MouseEvent event) throws IOException {
        JFileChooser picChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG photos only", "png");
        picChooser.setFileFilter(filter);
        picChooser.setApproveButtonText("Choose playlist photo");
        int returnVal = picChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String imagePath = picChooser.getSelectedFile().getPath();
            Image fixedSizeImage = new Image("file:\\\\\\" + imagePath, 100.0, 100.0, false, false);
            playlistPhoto.setImage(fixedSizeImage);
            picBlob = model.getBlob(fixedSizeImage);
        }
    }
    /**
     * This function remove songs from playlist.
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void removeChosen(MouseEvent event) throws IOException {
        Song removedSong = playlistSongsTable.getSelectionModel().getSelectedItem();
        c_chosenSongs.remove(removedSong);
        playlistSongsTable.setItems(FXCollections.observableArrayList(c_chosenSongs));
        removeChosen.setDisable(true);
    }
    /**
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void enableRemoveButton(MouseEvent event) throws IOException {
        Song chosenSong = playlistSongsTable.getSelectionModel().getSelectedItem();
        if (chosenSong != null) {
            removeChosen.setDisable(false);
        }
    }
    /**
     * This function create a new playlist.
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void createPlaylist(MouseEvent event) throws IOException {
        int creatorId = PlaylistChooserUIController.clientData.id;
        String name = playlistName.getText();
        String message;
        if ("".equals(name) || c_chosenSongs.isEmpty()) {
            message = "Error occured! please set playlist name and choose songs to it";
        } else {
            boolean res = model.uploadPlaylistToDB(name, picBlob, c_chosenSongs, creatorId);
            if (res == true) {
                message = "'" + name + "' playlist has been saved!";
                refreshPage("/tubit/views/PlaylistChooserUI.fxml");
            } else {
                message = "Error occured! playlist has not been saved";
            }
        }
        JOptionPane.showMessageDialog(null, message);
    }
    /**
     * This function get he search criteria
     * 
     * @return criteria if success, null otherwise.
     */
    private SEARCH_CRITERIA getSearchCriteria() {
        RadioButton selectedRadioButton = (RadioButton) songSearchingSubject.getSelectedToggle();
        String filter = selectedRadioButton.getText();
        switch (filter) {
            case "Song name":
                return SEARCH_CRITERIA.SONG_NAME;
            case "Singer name":
                return SEARCH_CRITERIA.SINGER_NAME;
            case "Album name":
                return SEARCH_CRITERIA.ALBUM_NAME;
        }
        // unreachable code.
        return null;
    }
    /**
     * This function bind between the table columns.
     */
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
    /**
     * This function remove songs that allready chosen from the playlist.
     */
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
