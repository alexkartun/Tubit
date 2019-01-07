package tubit.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import tubit.models.ClientData;
import tubit.models.PlaylistChooserModel;
import tubit.models.Playlist;
import tubit.models.PlaylistChooserModel.FILTER;

/**
 * This class play chooser playlist.
 * Extends TubitBaseController.
 * 
 */
public class PlaylistChooserUIController extends TubitBaseController {

    static Playlist chosenPlaylist;
    static ClientData clientData;
    private final int NUM_OF_SHOWN_PLAYLISTS = 4;
    final List<Playlist> c_moodsPlaylists;
    final List<Playlist> c_userPopularity;
    final List<Playlist> c_userRecent;
    final List<Playlist> c_userFavorites;
    int admin_playlist_current;
    int user_playlist_current;
    PlaylistChooserModel model;
    @FXML
    HBox adminPlaylists;
    @FXML
    HBox userPlaylists;
    @FXML
    ToggleGroup userPlaylistFilter;
    /**
     * Constractor
     */
    public PlaylistChooserUIController() {
        clientData = MainUIController.client;
        model = new PlaylistChooserModel();
        c_moodsPlaylists = model.getMoodPlaylists();
        c_userPopularity = model.getPopularPlaylists();
        c_userRecent = model.getRecentPlaylists();
        c_userFavorites = clientData.favoritePlaylists;
        admin_playlist_current = 0;
        user_playlist_current = 0;
    }
    /**
     * This function initializes the controller class.
     * 
     * @param location - (URL) 
     * @param resources - (ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDraggable();
        init(FILTER.ADMIN);
        init(getFilter());
    }
    /**
     * This function initialize the playlist chooser.
     */
    private void init(FILTER f) {
        switch (f) {
            case ADMIN:
                generateImageViews(adminPlaylists, c_moodsPlaylists, admin_playlist_current);
                break;
            case POPULARITY:
                generateImageViews(userPlaylists, c_userPopularity, user_playlist_current);
                break;
            case RECENT:
                generateImageViews(userPlaylists, c_userRecent, user_playlist_current); 
                break;
            case FAVORITE:
                generateImageViews(userPlaylists, c_userFavorites, user_playlist_current);
                break;

        }
    }
    /**
     * This function generate new ImageViews.
     * 
     * @param playlistsHbox - (HBox) 
     * @param playlists - (List[Playlist]) list of playlists.
     * @param offset - (int) 
     */
    private void generateImageViews(HBox playlistsHbox, List<Playlist> playlists, int offset) {
        playlistsHbox.getChildren().clear();
        for (int i = offset; i < playlists.size(); i++) {
            if (playlistsHbox.getChildren().size() == NUM_OF_SHOWN_PLAYLISTS) {
                break;
            }
            final Playlist playlist = playlists.get(i);
            playlistsHbox.getChildren().add(createImageView(playlist));
        }
    }
    /**
     * This function create new ImageView.
     * 
     * @param p -(Playlist) 
     * 
     * @return view - (ImageView) the image for the playlist.
     */
    private ImageView createImageView(Playlist p) {
        ImageView view = new ImageView(p.getImage());
        view.setFitWidth(80.0);
        view.setFitHeight(80.0);
        view.setOnMouseClicked((event) -> {
            try {
                chosenPlaylist = p;
                refreshPage("/tubit/views/PUI.fxml");
            } catch (IOException ex) {
                Logger.getLogger(PlaylistChooserUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return view;
    }
    /**
     * This function get the choosen FILTER.
     * 
     * @return filter - (FILTER)  
     */
    private FILTER getFilter() {
        RadioButton selectedRadioButton = (RadioButton) userPlaylistFilter.getSelectedToggle();
        String filter = selectedRadioButton.getText();
        switch (filter) {
            case "Popularity":
                return FILTER.POPULARITY;
            case "Recent":
                return FILTER.RECENT;
            case "Favorite":
                return FILTER.FAVORITE;
            default:
                return FILTER.ADMIN;
        }
    }
    /**
     *  This function change the current FILTER.
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void changeFilter(MouseEvent event) throws IOException {
        // back current position user playlists to start
        user_playlist_current = 0;
        init(getFilter());
    }
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
     * This functon handle the admin backward list
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void admin_backwardList(MouseEvent event) throws IOException {
        if (admin_playlist_current > 0) {
            admin_playlist_current--;
            init(FILTER.ADMIN);
        }
    }
    /**
     * This functon handle the admin forward list
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void admin_forwardList(MouseEvent event) throws IOException {
        if (admin_playlist_current + NUM_OF_SHOWN_PLAYLISTS < c_moodsPlaylists.size()) {
            admin_playlist_current++;
            init(FILTER.ADMIN);
            //initPlaylistMap(++admin_playlist_current, user_playlist_current, getUserFilter());
        }
    }
    /**
     * This functon handle the user backward list
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void user_backwardList(MouseEvent event) throws IOException {
        if (user_playlist_current > 0) {
            user_playlist_current--;
            init(getFilter());
        }
    }
    /**
     * This functon handle the user forward list
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void user_forwardList(MouseEvent event) throws IOException {
        int userPlaylistSize = getPlaylistSize();
        if (user_playlist_current + NUM_OF_SHOWN_PLAYLISTS < userPlaylistSize) {
            user_playlist_current--;
            init(getFilter());
        }
    }
    /**
     * This function get the playlist size.
     * 
     * @return (int) the number of songs in the playlist. 
     */
    private int getPlaylistSize() {
        FILTER f = getFilter();
        switch (f) {
            case POPULARITY:
                return c_userPopularity.size();
            case RECENT:
                return c_userRecent.size();
            case FAVORITE:
                return c_userFavorites.size();
        }
        return -1;
    }
    /**
     * This function get back the MakePlaylistUI.fxml
     * 
     * @param event - (MouseEvent) mouse click event
     * 
     * @throws IOException 
     */
    @FXML
    private void gotoPlaylistMaker(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/MakePlaylistUI.fxml");
    }
}
