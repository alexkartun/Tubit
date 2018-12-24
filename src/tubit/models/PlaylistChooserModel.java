/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

import db.DBUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.util.Pair;

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
        List<String> genres = DBUtils.getInstance().getAllGenres();
        m_moodsPlaylists = DBUtils.getInstance().getAdmidPlaylists(genres, initGenreToMoodMap());
        for (Playlist p : m_moodsPlaylists) {
            for (Song s : p.getSongsList()) {
                System.out.println(s.getName());
            }
        }
        /*list.add(new Playlist("1", "Gym", new Image("resources/images/gym.png")));
        list.add(new Playlist("2", "Sleep", new Image("resources/images/sleep.png")));
        list.add(new Playlist("3", "Party", new Image("resources/images/party.png")));
        list.add(new Playlist("4", "Smooth", new Image("resources/images/smooth.png")));
        list.add(new Playlist("5", "Feeling blue", new Image("resources/images/feeling_blue.png")));
        list.add(new Playlist("6", "Relax and happy", new Image("resources/images/relaxed.png")));
        list.add(new Playlist("7", "Death rock", new Image("resources/images/rock.png")));*/
        return m_moodsPlaylists;      
    }
    
    private Map<String, Pair<String, Image>> initGenreToMoodMap() {
        Map<String, Pair<String, Image>> map = new HashMap<>();
        map.put("rap", new Pair("Gym", new Image("resources/images/gym.png")));
        map.put("folk", new Pair("Sleep", new Image("resources/images/sleep.png")));
        map.put("hip-hop", new Pair("Party", new Image("resources/images/party.png")));
        map.put("r&b", new Pair("Feeling blue", new Image("resources/images/feeling_blue.png")));
        map.put("pop", new Pair("Relax and happy", new Image("resources/images/relaxed.png")));
        map.put("rock", new Pair("Death rock", new Image("resources/images/rock.png")));
        return map;
    }
}
