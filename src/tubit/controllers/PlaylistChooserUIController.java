/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
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
 * FXML Controller class
 *
 * @author Ofir
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDraggable();
        init(FILTER.ADMIN);
        init(getFilter());
    }

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

    @FXML
    private void changeFilter(MouseEvent event) throws IOException {
        // back current position user playlists to start
        user_playlist_current = 0;
        init(getFilter());
    }

    @FXML
    private void backToMenu(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/MainUI.fxml");
    }

    @FXML
    private void admin_backwardList(MouseEvent event) throws IOException {
        if (admin_playlist_current > 0) {
            admin_playlist_current--;
            init(FILTER.ADMIN);
        }
    }

    @FXML
    private void admin_forwardList(MouseEvent event) throws IOException {
        if (admin_playlist_current + NUM_OF_SHOWN_PLAYLISTS < c_moodsPlaylists.size()) {
            admin_playlist_current++;
            init(FILTER.ADMIN);
            //initPlaylistMap(++admin_playlist_current, user_playlist_current, getUserFilter());
        }
    }

    @FXML
    private void user_backwardList(MouseEvent event) throws IOException {
        if (user_playlist_current > 0) {
            user_playlist_current--;
            init(getFilter());
        }
    }

    @FXML
    private void user_forwardList(MouseEvent event) throws IOException {
        int userPlaylistSize = getPlaylistSize();
        if (user_playlist_current + NUM_OF_SHOWN_PLAYLISTS < userPlaylistSize) {
            user_playlist_current--;
            init(getFilter());
        }
    }
    
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

    @FXML
    private void gotoPlaylistMaker(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/MakePlaylistUI.fxml");
    }
}
