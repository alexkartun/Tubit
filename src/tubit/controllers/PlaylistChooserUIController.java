/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private final int NUM_OF_SHOWN_PLAYLISTS = 4;
    List<Playlist> c_moodsPlaylists;
    List<Playlist> c_userPopularity;
    List<Playlist> c_userRecent;
    int admin_playlist_current;
    int user_playlist_current;
    Map<ImageView, Playlist> playlistLinker;
    PlaylistChooserModel model;
    @FXML
    ImageView admin_pl1;
    @FXML
    ImageView admin_pl2;
    @FXML
    ImageView admin_pl3;
    @FXML
    ImageView admin_pl4;
    @FXML
    ImageView user_pl1;
    @FXML
    ImageView user_pl2;
    @FXML
    ImageView user_pl3;
    @FXML
    ImageView user_pl4;
    @FXML
    ToggleGroup userPlaylistFilter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDraggable();
        model = new PlaylistChooserModel();
        playlistLinker = new HashMap<>();
        admin_playlist_current = 0;
        user_playlist_current = 0;
        c_moodsPlaylists = model.getMoodPlaylists();
        c_userPopularity = model.getPopularPlaylists();
        c_userRecent = model.getRecentPlaylists();
        chosenPlaylist = null; // will be updated when user chooses playlist.
        initPlaylistMap(admin_playlist_current, user_playlist_current, getUserFilter());
    }

    private void initPlaylistMap(int admin_startIdx, int user_startIndex, FILTER f) {
        playlistLinker.put(admin_pl1, c_moodsPlaylists.get(admin_startIdx));
        playlistLinker.put(admin_pl2, c_moodsPlaylists.get(admin_startIdx + 1));
        playlistLinker.put(admin_pl3, c_moodsPlaylists.get(admin_startIdx + 2));
        playlistLinker.put(admin_pl4, c_moodsPlaylists.get(admin_startIdx + 3));
        switch (f) {
            case POPULARITY:
                playlistLinker.put(user_pl1, c_userPopularity.get(user_startIndex));
                playlistLinker.put(user_pl2, c_userPopularity.get(user_startIndex + 1));
                playlistLinker.put(user_pl3, c_userPopularity.get(user_startIndex + 2));
                playlistLinker.put(user_pl4, c_userPopularity.get(user_startIndex + 3));
                break;
            case RECENT:
                playlistLinker.put(user_pl1, c_userRecent.get(user_startIndex));
                playlistLinker.put(user_pl2, c_userRecent.get(user_startIndex + 1));
                playlistLinker.put(user_pl3, c_userRecent.get(user_startIndex + 2));
                playlistLinker.put(user_pl4, c_userRecent.get(user_startIndex + 3));
                break;
        }
        setPlaylistsImages();
    }

    private void setPlaylistsImages() {
        admin_pl1.setImage(playlistLinker.get(admin_pl1).getImage());
        admin_pl2.setImage(playlistLinker.get(admin_pl2).getImage());
        admin_pl3.setImage(playlistLinker.get(admin_pl3).getImage());
        admin_pl4.setImage(playlistLinker.get(admin_pl4).getImage());
        user_pl1.setImage(playlistLinker.get(user_pl1).getImage());
        user_pl2.setImage(playlistLinker.get(user_pl2).getImage());
        user_pl3.setImage(playlistLinker.get(user_pl3).getImage());
        user_pl4.setImage(playlistLinker.get(user_pl4).getImage());
    }

    private FILTER getUserFilter() {
        RadioButton selectedRadioButton = (RadioButton) userPlaylistFilter.getSelectedToggle();
        String filter = selectedRadioButton.getText();
        switch (filter) {
            case "Popularity":
                return FILTER.POPULARITY;
            case "Recent":
                return FILTER.RECENT;
        }
        // unreachable code.
        return null;
    }

    @FXML
    private void backToMenu(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/MainUI.fxml");
    }

    @FXML
    private void admin_backwardList(MouseEvent event) throws IOException {
        if (admin_playlist_current > 0) {
            playlistLinker.clear();
            initPlaylistMap(--admin_playlist_current, user_playlist_current, getUserFilter());
        }
    }

    @FXML
    private void admin_forwardList(MouseEvent event) throws IOException {
        if (admin_playlist_current + NUM_OF_SHOWN_PLAYLISTS < c_moodsPlaylists.size()) {
            playlistLinker.clear();
            initPlaylistMap(++admin_playlist_current, user_playlist_current, getUserFilter());
        }
    }

    @FXML
    private void user_backwardList(MouseEvent event) throws IOException {
        if (user_playlist_current > 0) {
            playlistLinker.clear();
            initPlaylistMap(admin_playlist_current, --user_playlist_current, getUserFilter());
        }
    }

    @FXML
    private void user_forwardList(MouseEvent event) throws IOException {
        switch (getUserFilter()) {
            case POPULARITY:
                if (user_playlist_current + NUM_OF_SHOWN_PLAYLISTS < c_userPopularity.size()) {
                    playlistLinker.clear();
                    initPlaylistMap(admin_playlist_current, ++user_playlist_current, getUserFilter());
                }
                break;
            case RECENT:
                if (user_playlist_current + NUM_OF_SHOWN_PLAYLISTS < c_userRecent.size()) {
                    playlistLinker.clear();
                    initPlaylistMap(admin_playlist_current, ++user_playlist_current, getUserFilter());
                }
                break;
        }
    }

    @FXML
    private void showPlaylist(MouseEvent event) throws IOException {
        ImageView clickedImage = (ImageView) event.getSource();
        chosenPlaylist = playlistLinker.get(clickedImage); // saves the chosen playlist for next window.
        refreshPage("/tubit/views/PlayerUI.fxml");
    }

    @FXML
    private void gotoPlaylistMaker(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/MakePlaylistUI.fxml");
    }
}
