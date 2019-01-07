package tubit.models;

import db.DBUtils;
import java.util.List;
import javafx.scene.image.Image;
/**
 * This class handle the model of the playlist.
 * 
 */
public class Playlist {
    private int id;
    private String creator;
    private String name;
    private Image icon;
    private int popularity;
    private List<Song> songsList;
    private boolean isAdminMade;

    /**
     * Constractor for user made playlists.
     * @param id - (int) the playlist id number.
     * @param creator - (String) the user name.
     * @param name - (String) the playlist name.
     * @param icon - (Image) the plailist image.
     * @param p - (int) the playlist popularity.
     * @param songs - (list[Song]) list of songs
     * @param isAdminMade - (boolean) true if it's an admin playlist, false otherwise.
     */
    public Playlist(int id, String creator, String name, Image icon, int p, List<Song> songs, boolean isAdminMade) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.icon = icon;
        this.popularity = p;
        this.songsList = songs;
        this.isAdminMade = isAdminMade;
    }
    /**
     * This class increase the populariy by 1.
     */
    public void increasePopularity() {
        DBUtils.getInstance().updatePlaylistPopularity(this.id, 1);
    }
    /**
     * This class decrease the populariy by 1.
     */
    public void decreasePopularity() {
        DBUtils.getInstance().updatePlaylistPopularity(this.id, -1);
    }
    
    /**
     * This function get the creator of the playlist.
     * 
     * @return (String) playlist creator name
     */
    public String getCreator() {
        return this.creator;
    }
    /**
     * This function get the popularity of the playlist.
     * 
     * @return (int) the playlist popularity
     */        
    public int getPopularity() {
        return this.popularity;
    }
    /**
     * This function get the ID of the playlist.
     * 
     * @return (int) the playlist ID.
     */   
    public int getID() {
        return this.id;
    }
     /**
     * This function check if the playlist creator us admin.
     * 
     * @return (boolean) true if it's an admin playlist, false otherwise.
     */   
    public boolean getIsAdmin() {
        return isAdminMade;
    }
    /**
     * This function get the name of the playlist.
     * 
     * @return (String) the playlist name.
     */ 
    public String getName() {
        return name;
    }
    /**
     * This function get the image of the playlist.
     * 
     * @return (Image) the playlist image.
     */ 
    public Image getImage() {
        return icon;
    }
    /**
     * This function get the songs list of the playlist.
     * 
     * @return (list[Song]) list of songs
     */ 
    public List<Song> getSongsList() {
        return songsList;
    }
}


