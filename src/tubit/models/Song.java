package tubit.models;
/**
 * This class handle the song model.
 * 
 */
public class Song {
    private int id;
    private String name;
    private String singer;
    private String album;
    private int duration;
    private int yearReleased;
    private String url;
    /**
     * Constractor
     * 
     * @param id - (int) the song id number.
     * @param name - (String) the song name.
     * @param duration - (int)
     * @param year - (int) the year that the song published.
     * @param singer - (String) the artist that sing the song.
     * @param album - (String) the album from which the songs come from.
     * @param url - (URL) youtube link of the song.
     */
    public Song(int id, String name, int duration, int year, String singer, String album, String url) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.yearReleased = year;
        this.singer = singer;
        this.album = album;
        this.url = url;
    }
    /**
     * This function get the song ID.
     * 
     * @return (int) the song id number.
     */
    public int getSongId() {
        return this.id;
    }
    /**
     * This function get the song url.
     * 
     * @return (URL) youtube link of the song.
     */
    public String getUrl() {
        return this.url;
    }
    /**
     * This function get the song name.
     * 
     * @return (String) the song name.
     */
    public String getSongName() {
        return name;
    }
    /**
     * This function get the singer name.
     * 
     * @return (String) the artist that sing the song.
     */
    public String getSingerName() {
        return singer;
    }
    /**
     * This function get the album name.
     * 
     * @return (String) the album from which the songs come from.
     */
    public String getAlbumName() {
        return album;
    }
    /**
     * This function get the song duration.
     * 
     * @return (int)
     */
    public int getDuration() {
        return duration;
    }
    /**
     * This function get the song year.
     * 
     * @return (int) the year that the song published.
     */
    public int getYear() {
        return yearReleased;
    }
}
