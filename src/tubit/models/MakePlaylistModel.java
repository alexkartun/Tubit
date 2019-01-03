/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import db.DBUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Ofir
 */
public class MakePlaylistModel {
    public enum SEARCH_CRITERIA {
        SONG_NAME, SINGER_NAME, ALBUM_NAME, DEFAULT;
    }
    public MakePlaylistModel() {
        
    }
    
    public List<Song> extractSongs(SEARCH_CRITERIA c, String searchingText) {
        return DBUtils.getInstance().getSongsByCriteria(c, searchingText);
        /*List<Song> testList = new ArrayList<>();
        testList.add(new Song(1,"Yeahh baby", 215, 2016, "DJ Kaled", "LEchkikon", null));
        testList.add(new Song(2,"One day in your mouth", 199, 2015, "dsads", "Yohanis", null));
        testList.add(new Song(3,"Me and you", 300, 2019, "TZOOZI", "Hopa", null));
        return testList;*/
    }
    
    String getAttribute(SEARCH_CRITERIA c) {
        switch (c) {
            case SONG_NAME:
                return "songs.name";
            case SINGER_NAME:
                return "singers.name";
            case ALBUM_NAME:
                return "albums.name";
            default:
                return "";
                
        }
    }
    
    public boolean uploadPlaylistToDB(String name, ByteArrayInputStream blob, List<Song> songs, int creatorId) {
        return DBUtils.getInstance().insertPlaylist(name, blob, songs, creatorId);
    }
    
    public ByteArrayInputStream getBlob(Image img) {
        ByteArrayOutputStream baos = null;
        try {
            BufferedImage bi = SwingFXUtils.fromFXImage(img, null);
            baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
        } catch (IOException ex) {
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
