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
public class Playlist {
    private int id;
    private String name;
    private Image icon;
    private int popularity;
    private List<Song> songsList;
    
    // for user made playlists
    public Playlist(int id, String name, Image icon, int p, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.popularity = p;
        songsList = songs;
    }
    
    // for admin playlists
    public Playlist(String name, Image img, List<Song> songs) {
        this.name = name;
        this.icon = img;
        this.popularity = -1;
        songsList = songs;
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


