package tubit.models;

import db.DBUtils;
import java.util.List;



/**
 * This class handle the model of the PlaylistChooser.
 * 
 */
public class PlaylistChooserModel {
    public enum FILTER {
        ADMIN, POPULARITY, RECENT, FAVORITE;
    }
    private List<Playlist> m_moodsPlaylists;
    private List<Playlist> m_popularPlayLists;
    private List<Playlist> m_recentPlaylists;
    
    /**
     * Constractor
     */
    public PlaylistChooserModel() {
        m_moodsPlaylists = extractPlaylist(true, FILTER.ADMIN);
        m_popularPlayLists = extractPlaylist(false, FILTER.POPULARITY);
        m_recentPlaylists = extractPlaylist(false, FILTER.RECENT);
    }
    /**
     * This function get the playlists by mood.
     *
     * @return (list[Song]) list of songs.
     */
    public List<Playlist> getMoodPlaylists() {
        return m_moodsPlaylists;
    }
    /**
     * This function get the playlists by popularity.
     *
     * @return (list[Song]) list of songs.
     */
    public List<Playlist> getPopularPlaylists() {
        return m_popularPlayLists;
    }
    /**
     * This function get the playlists by recent play time.
     *
     * @return (list[Song]) list of songs.
     */
    public List<Playlist> getRecentPlaylists() {
        return m_recentPlaylists;
    }
    
    /**
     * This function extract playlist.
     * 
     * @param isAdminMade - (boolean) true if it's an admin playlist, false otherwise.
     * @param f - (FILTER) enum 
     * 
     * @return  (list[Playlist]) list of playlists. 
     */
    private List<Playlist> extractPlaylist(boolean isAdmin, FILTER f) {
        return DBUtils.getInstance().getPlaylists(isAdmin, f); 
    }
}
