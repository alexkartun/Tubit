 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

import db.DBUtils;
import java.util.List;

/**
 *
 * @author Ofir
 */
public class PlaylistChooserModel {
    private List<Playlist> m_moodsPlaylists;
    private List<Playlist> m_popularPlayLists;
    private List<Playlist> m_recentPlaylists;
    
    
    public PlaylistChooserModel() {
        m_moodsPlaylists = extractAdminPlaylist();
        m_popularPlayLists = extractPlaylist(true);
        m_recentPlaylists = extractPlaylist(false);
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
        m_moodsPlaylists = DBUtils.getInstance().getPlaylists(true, true);
        return m_moodsPlaylists;
    }
    
    // popular = true, recent = false
    private List<Playlist> extractPlaylist(boolean isPopular) {
        List<Playlist> m_PlayLists = DBUtils.getInstance().getPlaylists(false, isPopular);
        return m_PlayLists; 
    }
}
