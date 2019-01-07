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
 * This class handle the model of making playlist.
 * 
 */
public class MakePlaylistModel {
    public enum SEARCH_CRITERIA {
        SONG_NAME, SINGER_NAME, ALBUM_NAME, DEFAULT;
    }
    /**
     * Default constractor.
     */
    public MakePlaylistModel() {
        
    }
    /**
     * This function extract songs from the DB. 
     *
     * @param c - (SEARCH_CRITERIA)
     * @param searchingText - (String) 
     *
     * @return (List[Song]) list of songs.
     */
    public List<Song> extractSongs(SEARCH_CRITERIA c, String searchingText) {
        return DBUtils.getInstance().getSongsByCriteria(c, searchingText);
    }
    /**
     * This function get the attribute.
     *
     * @param c - (SEARCH_CRITERIA)
     *
     * @return (String) SQL attribute
     */
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
    /**
     * This function upload new playlist to the DB.
     * 
     * @param name - (String) playlist name
     * @param blob - (ByteArrayInputStream) playlist image
     * @param songs - (list[Song]) list of songs
     * @param creatorId - (int) id number of the creator of the playlist
     * 
     * @return true if success , false otherwish.
     */
    public boolean uploadPlaylistToDB(String name, ByteArrayInputStream blob, List<Song> songs, int creatorId) {
        return DBUtils.getInstance().insertPlaylist(name, blob, songs, creatorId);
    }
    /**
     * This function get the blob.
     * 
     * @param img - (Image) 
     *
     * @return (Blob) image convarted
     */
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
