/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 *
 * @author Ofir
 */
public class PlaylistChooserModel {
    private List<Playlist> m_moodsPlaylists;

    public PlaylistChooserModel() {
        m_moodsPlaylists = extractPlaylist();
    }
    
    public List<Playlist> getMoodPlaylists() {
        return m_moodsPlaylists;
    }
    
    // TODO: recv from DB.
    private List<Playlist> extractPlaylist() {
        List<Playlist> list = new ArrayList<>();
        list.add(new Playlist("1", "Gym", new Image("resources/images/gym.png")));
        list.add(new Playlist("2", "Sleep", new Image("resources/images/sleep.png")));
        list.add(new Playlist("3", "Party", new Image("resources/images/party.png")));
        list.add(new Playlist("4", "Smooth", new Image("resources/images/smooth.png")));
        list.add(new Playlist("5", "Feeling blue", new Image("resources/images/feeling_blue.png")));
        list.add(new Playlist("6", "Relax and happy", new Image("resources/images/relaxed.png")));
        list.add(new Playlist("7", "Death rock", new Image("resources/images/rock.png")));
        return list;      
    }
}
