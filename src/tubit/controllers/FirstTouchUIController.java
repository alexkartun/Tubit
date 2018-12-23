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
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import tubit.models.PlaylistChooserModel;
import tubit.models.Playlist;

/**
 * FXML Controller class
 *
 * @author Ofir
 */

        
public class FirstTouchUIController extends TubitBaseController {
    private final int NUM_OF_SHOWN_PLAYLISTS = 4;
    List<Playlist> c_moodsPlaylists;
    int playlist_current;
    Map<ImageView, Playlist> playlistLinker;
    PlaylistChooserModel model;
    @FXML
    ImageView pl1;
    @FXML
    ImageView pl2;
    @FXML
    ImageView pl3;
    @FXML
    ImageView pl4;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDraggable();
        model = new PlaylistChooserModel();
        playlistLinker = new HashMap<>();
        playlist_current = 0;
        c_moodsPlaylists = model.getMoodPlaylists();
        initPlaylistMap();
    }
    
    private void initPlaylistMap() {
        playlistLinker.put(pl1, c_moodsPlaylists.get(0));
        playlistLinker.put(pl2, c_moodsPlaylists.get(1));
        playlistLinker.put(pl3, c_moodsPlaylists.get(2));
        playlistLinker.put(pl4, c_moodsPlaylists.get(3));
        setPlaylistsImages();
    }
    
    private void setPlaylistsImages() {
        pl1.setImage(playlistLinker.get(pl1).getImage());
        pl2.setImage(playlistLinker.get(pl2).getImage());
        pl3.setImage(playlistLinker.get(pl3).getImage());
        pl4.setImage(playlistLinker.get(pl4).getImage());
    }
    
    @FXML
    private void backToMenu(MouseEvent event) throws IOException {
        AnchorPane pane = (AnchorPane) FXMLLoader.load(getClass().getResource("/tubit/views/LoginUI.fxml"));
        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(pane);
    }
}
