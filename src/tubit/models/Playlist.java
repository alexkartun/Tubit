/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

import db.DBUtils;
import java.util.List;
import javafx.scene.image.Image;
/**
 *
 * @author Ofir
 */
public class Playlist {
    private int id;
    private String creator;
    private String name;
    private Image icon;
    private int popularity;
    private List<Song> songsList;
    private boolean isAdminMade;
    // for user made playlists
    public Playlist(int id, String creator, String name, Image icon, int p, List<Song> songs, boolean isAdminMade) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.icon = icon;
        this.popularity = p;
        this.songsList = songs;
        this.isAdminMade = isAdminMade;
    }
    
    public void increasePopularity() {
        DBUtils.getInstance().updatePlaylistPopularity(this.id, 1);
    }
    
    public void decreasePopularity() {
        DBUtils.getInstance().updatePlaylistPopularity(this.id, -1);
    }
    
    
    public String getCreator() {
        return this.creator;
    }
            
    public int getPopularity() {
        return this.popularity;
    }
    
    public int getID() {
        return this.id;
    }
    
    public boolean getIsAdmin() {
        return isAdminMade;
    }
    
    public String getName() {
        return name;
    }
    
    public Image getImage() {
        return icon;
    }
    
    public List<Song> getSongsList() {
        return songsList;
    }
    
}


