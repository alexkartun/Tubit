/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

import java.util.List;
import javafx.scene.image.Image;
/**
 *
 * @author Ofir
 */
public class Playlist {
    private String id;
    private String name;
    private Image icon;
    private List<Song> songsList;
    
    public Playlist(String id, String name, Image icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
    
    public String getName() {
        return name;
    }
    
    public Image getImage() {
        return icon;
    }
}


