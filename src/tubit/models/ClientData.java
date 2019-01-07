package tubit.models;

import db.DBUtils;
import java.util.List;

/**
 * This class handle the model of client data.
 * 
 */
public class ClientData {
    public final int id; 
    public final boolean status; 
    public List<Playlist> favoritePlaylists;
    /**
     * Constractor.
     * 
     * @param id - (int) the user id number.
     * @param status - (boolean)
     * @param favoritePlaylists - (List[Playlist]) user favorite playlists.
     */
    public ClientData(int id, boolean status, List<Playlist> favoritePlaylists) { 
        this.id = id; 
        this.status = status; 
        this.favoritePlaylists = favoritePlaylists;
    } 
    /**
     * This function check if the user have favorite playlists.
     * 
     * @param chosenPlaylist - (Playlist) 
     * 
     * @return true if there is a favorite playlist, false otherwise.
     */
    public boolean checkForFavoritePlaylist(Playlist chosenPlaylist) {
        return favoritePlaylists.stream().anyMatch((playlist) -> (playlist.getID() == chosenPlaylist.getID()));
            
    }
    /**
     * This function add choosen playlist to the favorite playlist list of the user.
     * 
     * @param chosenPlaylist - (Playlist)
     */
    public void addFavoritePlaylist(Playlist chosenPlaylist) {
        favoritePlaylists.add(chosenPlaylist);
        DBUtils.getInstance().addFavoritePlaylistInDB(id, chosenPlaylist.getID());
    }
    /**
     * This function remove choosen playlist from the favorite playlist list of the user.
     * 
     * @param chosenPlaylist - (Playlist)
     */
    public void RemoveFavoritePlaylist(Playlist chosenPlaylist) {
        favoritePlaylists.remove(getPlaylistIndex(chosenPlaylist));
        DBUtils.getInstance().removeFavoritePlaylistsDB(id, chosenPlaylist.getID());
    }
    /**
     * This function get the playlist index.
     * 
     * @param chosenPlaylist- (Playlist)
     * 
     * @return index - (int) the playlist index.
     */
    private int getPlaylistIndex(Playlist chosenPlaylist) {
        int index = 0;
        for (Playlist playlist : favoritePlaylists) {
            if (playlist.getID() == chosenPlaylist.getID()) {
                break;
            }
            index++;
        }
        return index;
    }
}
