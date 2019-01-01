package db;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import tubit.models.MakePlaylistModel;
import tubit.models.Playlist;
import tubit.models.Playlist;
import tubit.models.PlaylistChooserModel.FILTER;
import tubit.models.Song;
import tubit.models.Song;

public class DBUtils {

    private static DBUtils instance;
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:3306/tubitdb?useSSL=false";
    private final String USER = "root";
    private final String PASS = "204717664";

    /**
     *
     * @return
     */
    public static DBUtils getInstance() {
        if (instance == null) {
            instance = new DBUtils();
        }
        return instance;
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public boolean checkClientInDB(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT COUNT(*) as TOTAL FROM clients WHERE username=? AND password=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            result.next();
            if (result.getInt("TOTAL") != 1) {
                return false;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error on creating connection or query execution...");
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("Error on closing statement...");
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println("Error on closing connection...");
                }
            }
        }
        return true;
    }

    /**
     *
     * @param username
     * @param email
     * @param password
     * @return
     */
    public boolean updateDB(String username, String email, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO clients(username,email,password) VALUES(?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error on creating connection or query execution...");
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("Error on closing statement...");
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println("Error on closing connection...");
                }
            }
        }
        return true;
    }

    public List<String> getAllGenres() {
        List<String> genres = null;

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT DISTINCT genre FROM singers";
            statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            // query excuted correctly
            genres = new ArrayList<>();
            while (result.next() == true) {
                genres.add(result.getString("genre"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error on creating connection or query execution...");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("Error on closing statement...");
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println("Error on closing connection...");
                }
            }
        }
        return genres;
    }
    
    public List<Song> getSongsByCriteria(String criteria, String searchField) {
        return null;
    }

    public List<Playlist> getPlaylists(boolean isAdmin, FILTER f) {
        List<Playlist> playlists = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            String playListsSql = null;
            if (isAdmin) {
                playListsSql = "SELECT playlists.playListId, playlists.playListName, playlists.popularity, playlists.playListImage\n"
                        + "FROM playlists\n"
                        + "WHERE isAdminMade = ?";
            } else if (f == FILTER.POPULARITY) {
                playListsSql = "SELECT playlists.playListId, playlists.playListName, playlists.popularity, playlists.playListImage\n"
                        + "FROM playlists\n"
                        + "WHERE isAdminMade = ? AND playlists.popularity > (SELECT AVG(playlists.popularity)\n"
                        + "FROM playlists)";
            } else if (f == FILTER.RECENT) {
                playListsSql = "SELECT playlists.playListId, playlists.playListName, playlists.popularity, playlists.playListImage\n"
                        + "FROM playlists\n"
                        + "WHERE isAdminMade = ? AND playlists.playlistId > (SELECT AVG(playlists.playlistId)\n"
                        + "FROM playlists)";
            }
            statement = connection.prepareStatement(playListsSql);
            statement.setBoolean(1, isAdmin);
            ResultSet playListResult = statement.executeQuery();
            while (playListResult.next() == true) {
                List<Song> songs = new ArrayList<>();
                String songsSql = "SELECT songs.songId, songs.songName, songs.songDuration, songs.yearReleased, singers.singerName, albums.albumName\n"
                        + "FROM songs, singers, albums, playlists_songs, playlists\n"
                        + "WHERE playlists_songs.playlistId = ? AND songs.songId = playlists_songs.songId AND songs.albumId = albums.albumId AND albums.singerId = singers.id";
                statement = connection.prepareStatement(songsSql);
                statement.setInt(1, playListResult.getInt("playListId"));
                ResultSet songsResult = statement.executeQuery();
                while (songsResult.next() == true) {
                    Song song = new Song(songsResult.getInt("songId"), songsResult.getString("songName"),
                            songsResult.getInt("songDuration"), songsResult.getInt("yearReleased"),
                            songsResult.getString("singerName"), songsResult.getString("albumName"));
                    songs.add(song);
                }
                playlists.add(createPlaylistFromDB(playListResult, songs, isAdmin));
            }

        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error on creating connection or query execution...");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("Error on closing statement...");
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println("Error on closing connection...");
                }
            }
        }
        return playlists;
    }

    private Playlist createPlaylistFromDB(ResultSet res, List<Song> songs, boolean isAdmin) {
        Playlist p = null;
        try {
            int id = res.getInt("playListId");
            String name = res.getString("playListName");
            Image img = convertBlobToImage(res.getBlob("playlistImage"));
            int popularity = res.getInt("popularity");
            p = new Playlist(id, name, img, popularity, songs, isAdmin);
        } catch (SQLException e) {
        }
        return p;
    }

    private Image convertBlobToImage(Blob blob) {
        InputStream is = null;
        try {
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            is = new ByteArrayInputStream(blobAsBytes);
        } catch (SQLException e) {}
        return new Image(is);
    }
}
//Image img = new Image(ImageIO.read(new ByteArrayInputStream(
//playListResult.getBlob("playlistImg").getBytes(1, playListResult.getBlob("playlistImg").length()))))
