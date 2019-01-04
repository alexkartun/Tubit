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
        ADMIN, POPULARITY, RECENT, FAVORITE;
    }
    private List<Playlist> m_moodsPlaylists;
    private List<Playlist> m_popularPlayLists;
    private List<Playlist> m_recentPlaylists;
    
    
    public PlaylistChooserModel() {
        m_moodsPlaylists = extractPlaylist(true, FILTER.ADMIN);
        m_popularPlayLists = extractPlaylist(false, FILTER.POPULARITY);
        m_recentPlaylists = extractPlaylist(false, FILTER.RECENT);
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
    
    
    private List<Playlist> extractPlaylist(boolean isAdmin, FILTER f) {
        return DBUtils.getInstance().getPlaylists(isAdmin, f); 
    }
}
