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
import javax.imageio.ImageIO;

/**
 *
 * @author Ofir
 */
public class MakePlaylistModel {
    public enum SEARCH_CRITERIA {
        SONG_NAME, SINGER_NANE, ALBUM_NAME;
    }
    public MakePlaylistModel() {
        
    }
    
    public List<Song> extractSongs(SEARCH_CRITERIA c, String searchingText) {
        //return DBUtils.getInstance().getSongsByCriteria(getAttribute(c), searchingText);
        List<Song> testList = new ArrayList<>();
        testList.add(new Song(1,"Yeahh baby", 215, 2016, "DJ Kaled", "LEchkikon", null));
        testList.add(new Song(2,"One day in your mouth", 199, 2015, "dsads", "Yohanis", null));
        testList.add(new Song(3,"Me and you", 300, 2019, "TZOOZI", "Hopa", null));
        return testList;
    }
    
    String getAttribute(SEARCH_CRITERIA c) {
        String s = c.toString().toLowerCase();
        String[] words = s.split("_");
        for(int i=1; i<words.length; i++) {
            // capitalized first letter.
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1); 
        }
        StringBuilder sb = new StringBuilder();
        for(String w : words)   sb.append(w);
        return sb.toString();
    }
    /**
    public ByteArrayInputStream getBias(String path) {
        ByteArrayOutputStream baos = null;
        try {
            BufferedImage bi = ImageIO.read(new File(getClass().getResource(path).toURI()));
            baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
        } catch (IOException ex) {
        } catch (URISyntaxException ex) {
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }
    **/
}
