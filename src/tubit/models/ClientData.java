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
 * @author Kartun
 */
public class ClientData {
    public final int id; 
    public final boolean status; 
    public List<Playlist> favoritePlaylists;
    public ClientData(int id, boolean status, List<Playlist> favoritePlaylists) { 
        this.id = id; 
        this.status = status; 
        this.favoritePlaylists = favoritePlaylists;
    } 
    
    public boolean checkForFavoritePlaylist(Playlist chosenPlaylist) {
        return favoritePlaylists.stream().anyMatch((playlist) -> (playlist.getID() == chosenPlaylist.getID()));
            
    }
    
    public void addFavoritePlaylist(Playlist chosenPlaylist) {
        favoritePlaylists.add(chosenPlaylist);
        DBUtils.getInstance().addFavoritePlaylistInDB(id, chosenPlaylist.getID());
    }
    
    public void RemoveFavoritePlaylist(Playlist chosenPlaylist) {
        favoritePlaylists.remove(getPlaylistIndex(chosenPlaylist));
        DBUtils.getInstance().removeFavoritePlaylistsDB(id, chosenPlaylist.getID());
    }
    
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
