 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

import db.DBUtils;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;



/**
 *
 * @author Ofir
 */
public class PlaylistChooserModel {
    public enum FILTER {
        POPULARITY, RECENT;
    }
    private List<Playlist> m_moodsPlaylists;
    private List<Playlist> m_popularPlayLists;
    private List<Playlist> m_recentPlaylists;
    
    
    public PlaylistChooserModel() {
        m_moodsPlaylists = extractAdminPlaylist();
        m_popularPlayLists = extractPlaylist(FILTER.POPULARITY);
        m_recentPlaylists = extractPlaylist(FILTER.RECENT);
    }
    
    public List<Playlist> getMoodPlaylists() {
        return m_moodsPlaylists;
    }
    
    public List<Playlist> getPopularPlaylists() {
        return m_popularPlayLists;
    }
    
    public List<Playlist> getRecentPlaylists() {
        return m_recentPlaylists;
    }
    
    private List<Playlist> extractAdminPlaylist() {
        return DBUtils.getInstance().getPlaylists(true, null);
    }
    
    
    private List<Playlist> extractPlaylist(FILTER f) {
        //return DBUtils.getInstance().getPlaylists(false, f); 
        //nt id, String name, Image icon, int p, List<Song> songs, boolean isAdminMade
        List<Playlist> list = new ArrayList<>();
        list.add(new Playlist(1, "Lechkikon", new Image("resources/images/u1.jpg"), 15, null, false));
        list.add(new Playlist(2, "Woolfi", new Image("resources/images/u2.jpg"), 20, null, false));
        list.add(new Playlist(3, "Tzookikon", new Image("resources/images/u3.jpg"), 25, null, false));
        list.add(new Playlist(4, "Yesss", new Image("resources/images/u4.jpg"), 30, null, false));
        return list;
    }
}
