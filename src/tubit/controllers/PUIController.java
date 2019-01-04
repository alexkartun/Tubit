/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import tubit.models.Playlist;
import tubit.models.Song;

/**
 * FXML Controller class
 *
 * @author Kartun
 */
public class PUIController extends TubitBaseController {

    @FXML
    private Pane pane;
    @FXML
    private Label rateLbl;
    @FXML
    private Label playlistName;
    @FXML
    private Label playlistCreator;
    @FXML
    private TableView<Song> songs;
    @FXML
    private TableColumn<Song, String> songName;
    @FXML
    private TableColumn<Song, String> singerName;
    @FXML
    private TableColumn<Song, String> albumName;
    @FXML
    private TableColumn<Song, Integer> duration;
    @FXML
    private TableColumn<Song, Integer> year;
    private WebView youtubeWebView;
    @FXML
    private FontAwesomeIcon rateBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDraggable();
        init();
    }

    private void init() {
        if (PlaylistChooserUIController.chosenPlaylist.getIsAdmin()) {
            rateLbl.setVisible(false);
            rateBtn.setVisible(false);
        } else {
            if (PlaylistChooserUIController.clientData.checkForFavoritePlaylist(PlaylistChooserUIController.chosenPlaylist)) {
                rateBtn.setOpacity(1.0);
            }
        }
        playlistName.setText(PlaylistChooserUIController.chosenPlaylist.getName());
        playlistCreator.setText(PlaylistChooserUIController.chosenPlaylist.getCreator());
        bindTablesColumns();
        fillPlaylistTable();
    }

    private void bindTablesColumns() {
        songName.setCellValueFactory(new PropertyValueFactory<>("songName"));
        singerName.setCellValueFactory(new PropertyValueFactory<>("singerName"));
        albumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
    }

    private void fillPlaylistTable() {
        songs.setItems(FXCollections.observableArrayList(PlaylistChooserUIController.chosenPlaylist.getSongsList()));
    }

    @FXML
    private void backToPlaylistChooser(MouseEvent event) throws IOException {
        refreshPage("/tubit/views/PlaylistChooserUI.fxml");
    }
    
    private void playSong(Song chosenSong) {
        if (chosenSong != null) {
            if (youtubeWebView == null) {
                youtubeWebView = new WebView();
                youtubeWebView.setLayoutX(30);
                youtubeWebView.setLayoutY(318);
                youtubeWebView.setPrefHeight(261);
                youtubeWebView.setPrefWidth(504);
                pane.getChildren().addAll(youtubeWebView);
            }
            youtubeWebView.getEngine().load(chosenSong.getUrl());
        }
    }
    
    @FXML
    private void playSongFirstOption(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Song chosenSong = songs.getSelectionModel().getSelectedItem();
            playSong(chosenSong);
        }
    }
    
    @FXML
    private void playSongSecondOption(MouseEvent event) {
        Song chosenSong = songs.getSelectionModel().getSelectedItem();
        playSong(chosenSong);
    }

    @FXML
    private void ratePlaylist(MouseEvent event) {
        double opacity = 1.0;
        if (PlaylistChooserUIController.clientData.checkForFavoritePlaylist(PlaylistChooserUIController.chosenPlaylist)) {
            PlaylistChooserUIController.chosenPlaylist.decreasePopularity();
            PlaylistChooserUIController.clientData.RemoveFavoritePlaylist(PlaylistChooserUIController.chosenPlaylist);
            opacity = 0.3;
        } else {
            PlaylistChooserUIController.chosenPlaylist.increasePopularity();
            PlaylistChooserUIController.clientData.addFavoritePlaylist(PlaylistChooserUIController.chosenPlaylist);
        }
        rateBtn.setOpacity(opacity);
    }
}
