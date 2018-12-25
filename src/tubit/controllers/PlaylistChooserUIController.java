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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import tubit.models.PlaylistChooserModel;
import tubit.models.Playlist;

/**
 * FXML Controller class
 *
 * @author Ofir
 */

        
public class PlaylistChooserUIController extends TubitBaseController {
    static Playlist chosenPlaylist;
    private final int NUM_OF_SHOWN_PLAYLISTS = 4;
    List<Playlist> c_moodsPlaylists;
    List<Playlist> c_userPlaylists;
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDraggable();
        model = new PlaylistChooserModel();
        playlistLinker = new HashMap<>();
        admin_playlist_current = 0;
        user_playlist_current = 0;
        c_moodsPlaylists = model.getMoodPlaylists();
        c_userPlaylists = model.getPopularPlaylists();
        chosenPlaylist = null; // will be updated when user chooses playlist.
        initPlaylistMap(admin_playlist_current, user_playlist_current);
    }
    
    private void initPlaylistMap(int admin_startIdx, int user_startIndex) {
        playlistLinker.put(admin_pl1, c_moodsPlaylists.get(admin_startIdx));
        playlistLinker.put(admin_pl2, c_moodsPlaylists.get(admin_startIdx+1));
        playlistLinker.put(admin_pl3, c_moodsPlaylists.get(admin_startIdx+2));
        playlistLinker.put(admin_pl4, c_moodsPlaylists.get(admin_startIdx+3));
        playlistLinker.put(user_pl1, c_userPlaylists.get(user_startIndex));
        playlistLinker.put(user_pl2, c_userPlaylists.get(user_startIndex+1));
        playlistLinker.put(user_pl3, c_userPlaylists.get(user_startIndex+2));
        playlistLinker.put(user_pl4, c_userPlaylists.get(user_startIndex+3));
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
    
    @FXML
    private void backToMenu(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/MainUI.fxml");
    }
    
    @FXML
    private void admin_backwardList(MouseEvent event) throws IOException {
        if (admin_playlist_current > 0) {
            playlistLinker.clear();
            initPlaylistMap(--admin_playlist_current, user_playlist_current);
        }
    }
    
    @FXML
    private void admin_forwardList(MouseEvent event) throws IOException {
        if (admin_playlist_current + NUM_OF_SHOWN_PLAYLISTS < c_moodsPlaylists.size()) {
            playlistLinker.clear();
            initPlaylistMap(++admin_playlist_current, user_playlist_current);
        }
    }
    
    @FXML
    private void user_backwardList(MouseEvent event) throws IOException {
        if (user_playlist_current > 0) {
            playlistLinker.clear();
            initPlaylistMap(admin_playlist_current, --user_playlist_current);
        }
    }
    
    @FXML
    private void user_forwardList(MouseEvent event) throws IOException {
        if (user_playlist_current + NUM_OF_SHOWN_PLAYLISTS < c_userPlaylists.size()) {
            playlistLinker.clear();
            initPlaylistMap(admin_playlist_current, ++user_playlist_current);
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
