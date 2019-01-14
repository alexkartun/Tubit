package db;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import org.codehaus.jackson.map.ObjectMapper;
import tubit.models.ClientData;
import tubit.models.MakePlaylistModel;
import tubit.models.Playlist;
import tubit.models.PlaylistChooserModel.FILTER;
import tubit.models.Song;
/**
 * DBUtils class
 * 
 * This class handle the database.
 * 
 */
public class DBUtils {
    // singleton
    private static DBUtils instance;
    // path for DB configurations file.
    private final String DBCONFIG_PATH = "config.json";
    // default properties for config.
    private final String DEFAULT_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DEFAULT_DB_URL = "jdbc:mysql://127.0.0.1:3306/team13?useSSL=false";
    private final String DEFAULT_USER = "team13";
    private final String DEFAULT_PASS = "1ax3p";
    
    private DBConfig config;
    /**
     * Singleton function for the DBUtils member 'instance'.
     * 
     * @return instance - (DBUtils) 
     */
    public static DBUtils getInstance() {
        if (instance == null) {
            instance = new DBUtils();
        }
        return instance;
    }
    
    private DBUtils() {
        if (new File(DBCONFIG_PATH).exists()) {
            loadConfig();
        } else {
            saveConfig();
        }
    }
    
    /**
     * reads DB configuration file and sets "config" member. 
     */
    private void loadConfig() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            config = mapper.readValue(new File(DBCONFIG_PATH), DBConfig.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * save configuration in a file as a json.
     */
    private void saveConfig() {
        ObjectMapper mapper = new ObjectMapper();
        config = new DBConfig(DEFAULT_USER, DEFAULT_PASS, DEFAULT_DB_URL);
        try {
            mapper.writeValue(new File(DBCONFIG_PATH), config);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This function check if the username and password are valid and exist in the DB:
     * if it is a new user - insert the ClientData to the DB,
     * if it is a exist user - load the user info from the DB.
     * 
     * @param username - (String) the name of the user.
     * @param password - (String) the password of the user.
     * @return clientData - (ClientData) info about the user.
     */
    public synchronized ClientData checkClientInDB(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ClientData clientData = null;
        List<Playlist> playlists = new ArrayList<>();
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
            String sql = "SELECT clients.id, COUNT(*) as TOTAL FROM clients WHERE username=? AND password=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            result.next();
            if (result.getInt("TOTAL") != 1) {
                return new ClientData(0, false, null);
            }
            String playListsSql = "SELECT favorite_playlists.playlistId\n"
                    + "FROM favorite_playlists\n"
                    + "WHERE clientId = ?";
            statement = connection.prepareStatement(playListsSql);
            statement.setInt(1, result.getInt("id"));
            ResultSet playListResult = statement.executeQuery();

            while (playListResult.next() == true) {

                playListsSql = "SELECT clients.username, playlists.id, playlists.name, playlists.popularity, playlists.is_admin_made, playlists.image\n"
                        + "FROM playlists INNER JOIN clients ON clients.id = playlists.creatorId\n"
                        + "WHERE playlists.id = ?";
                statement = connection.prepareStatement(playListsSql);
                statement.setInt(1, playListResult.getInt("playlistId"));
                ResultSet pLResult = statement.executeQuery();
                pLResult.next();
                List<Song> songs = createPlaylistSongs(pLResult, connection, statement);
                playlists.add(createPlaylistFromDB(pLResult, songs));
            }
            clientData = new ClientData(result.getInt("id"), true, playlists);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error on creating connection or query execution...");
            return new ClientData(0, false, null);
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
        return clientData;
    }
    /**
     * This function update the playlist popularity rank .
     * 
     * @param playlistId - (int) playlist identifier.
     * @param change - (int) possible values 1,-1.
     */
    public synchronized void updatePlaylistPopularity(int playlistId, int change) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
            String sql = "UPDATE playlists SET popularity = popularity + ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, change);
            statement.setInt(2, playlistId);
            statement.executeUpdate();
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
    }
    /**
     * This function insert new playlist to the DB and save the client identifier.
     * 
     * @param clientId -(int) client identifier.
     * @param playlistId -(int) playlist identifier.
     */
    public synchronized void addFavoritePlaylistInDB(int clientId, int playlistId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
            String sql = "INSERT INTO favorite_playlists(clientId,playlistId) VALUES(?,?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            statement.setInt(2, playlistId);
            statement.executeUpdate();
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
    }
    /**
     * This function remove an existing playlist from the database.
     * 
     * @param clientId -(int) client identifier.
     * @param playlistId -(int) playlist identifier.
     */
    public synchronized void removeFavoritePlaylistsDB(int clientId, int playlistId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
            String sql = "DELETE FROM favorite_playlists WHERE favorite_playlists.clientId = ? AND favorite_playlists.playlistId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            statement.setInt(2, playlistId);
            statement.executeUpdate();
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
    }
    /**
     * This function update the user info in the DB.
     * 
     * @param username - (String) username identifier.
     * @param email - (String) username email address.
     * @param password - (String) username password.
     * 
     * @return true if the function success and false otherwise.
     */
    public synchronized boolean updateDB(String username, String email, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
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
   /**
     * This function collect all types of genres from the DB. 
     * 
     * @return genres - (List[String]) list of all genres.
     */
    public synchronized List<String> getAllGenres() {
        List<String> genres = null;

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
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
    
    /**
     * This function return list of song by critera(song/album/singer name) 
     * and a searchFild string insert by the user.
     * 
     * @param c - (String) determine which kind of type the search would be from the option(song name/album name/artist name).
     * @param searchField - (String) text that the user want to search in the DB.
     * 
     * @return selectedSongs - (List[Song]) list of songs that match the by the criteria and the searchField.  
     */
    public List<Song> getSongsByCriteria(MakePlaylistModel.SEARCH_CRITERIA c, String searchField) {
        List<Song> selectedSongs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
            String sql = getSQLViaCriteria(c);
            statement = connection.prepareStatement(sql);
            statement.setString(1, '%' + searchField + '%');
            ResultSet result = statement.executeQuery();
            // query excuted correctly
            while (result.next() == true) {
                selectedSongs.add(getNextSong(result));
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
        return selectedSongs;
    }
    /**
     * This function return SQL quere by Criteria.
     * 
     * @param c - (Criteria)
     * 
     * @return (String) valid SQL quere. 
     */
    private String getSQLViaCriteria(MakePlaylistModel.SEARCH_CRITERIA c) {
        switch (c) {
            case SONG_NAME:
                return "SELECT songs.*, singers.name AS singerName, albums.name AS albumName\n"
                        + "FROM songs, singers, albums\n"
                        + "WHERE songs.singerId=singers.id AND\n"
                        + "songs.albumId=albums.id AND songs.name LIKE ?";
            case SINGER_NAME:
                return "SELECT songs.*, singers.name AS singerName, albums.name AS albumName\n"
                        + "FROM songs, singers, albums\n"
                        + "WHERE songs.singerId=singers.id AND\n"
                        + "songs.albumId=albums.id AND singers.name LIKE ?";
            case ALBUM_NAME:
                return "SELECT songs.*, singers.name AS singerName, albums.name AS albumName\n"
                        + "FROM songs, singers, albums\n"
                        + "WHERE songs.singerId=singers.id AND\n"
                        + "songs.albumId=albums.id AND albums.name LIKE ?";
            default:
                return "";
        }
    }
    /**
     * This function return the next song.
     * 
     * @param result -(ResultSet) 
     *
     * @return s - (Song) next song
     */
    private Song getNextSong(ResultSet result) {
        Song s = null;
        try {
            int id = result.getInt("id");
            String name = result.getString("name");
            int duration = result.getInt("duration");
            int year = result.getInt("year_released");
            String singer = result.getString("singerName");
            String album = result.getString("albumName");
            String url = result.getString("url");
            s = new Song(id, name, duration, year, singer, album, url);
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
    /**
     * This function return the playlist list from which the user could choose.
     * 
     * @param isAdmin - (boolean) true if it's an admin playlist, false otherwise.
     * @param f - (FILTER) could get the values: POPULARITY, RECENT or FAVORITE.
     * 
     * @return playlists - (List[Playlist]) list of possible playlists.
     */
    public synchronized List<Playlist> getPlaylists(boolean isAdmin, FILTER f) {
        List<Playlist> playlists = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
            String playListsSql = null;
            if (null != f) switch (f) {
                case ADMIN:
                    playListsSql = "SELECT clients.username, playlists.id, playlists.name, playlists.popularity, playlists.is_admin_made, playlists.image\n"
                            + "FROM playlists INNER JOIN clients ON clients.id = playlists.creatorId\n"
                            + "WHERE is_admin_made = ?";
                    break;
                case POPULARITY:
                    playListsSql = "SELECT clients.username, playlists.id, playlists.name, playlists.popularity, playlists.is_admin_made, playlists.image\n"
                            + "FROM playlists INNER JOIN clients ON clients.id = playlists.creatorId\n"
                            + "WHERE is_admin_made = ? AND playlists.popularity >= (SELECT AVG(playlists.popularity)\n"
                            + "FROM playlists WHERE is_admin_made = ?)";
                    break;
                case RECENT:
                    playListsSql = "SELECT clients.username, playlists.id, playlists.name, playlists.popularity, playlists.is_admin_made, playlists.image\n"
                            + "FROM playlists INNER JOIN clients ON clients.id = playlists.creatorId\n"
                            + "WHERE is_admin_made = ? AND playlists.id >= (SELECT AVG(playlists.id)\n"
                            + "FROM playlists WHERE is_admin_made = ?)";
                    break;
                default:
                    break;
            }
            statement = connection.prepareStatement(playListsSql);
            statement.setBoolean(1, isAdmin);
            if (!isAdmin)
                statement.setBoolean(2, isAdmin);
            ResultSet playListResult = statement.executeQuery();
            while (playListResult.next() == true) {
                List<Song> songs = createPlaylistSongs(playListResult, connection, statement);
                playlists.add(createPlaylistFromDB(playListResult, songs));
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
    /**
     * This function create new playlist.
     * 
     * @param res - (ResultSet) 
     * @param connection - (Connection)
     * @param statement - (PreparedStatement)
     *
     * @return songs - (List[song]) list of songs that create the playlist.
     * 
     * @throws SQLException 
     */
    private List<Song> createPlaylistSongs(ResultSet res, Connection connection, PreparedStatement statement) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String songsSql = "SELECT songs.id, songs.name, songs.duration, songs.year_released, songs.url, singers.name, albums.name\n"
                + "FROM songs, singers, albums, playlists_songs\n"
                + "WHERE playlists_songs.playlistId = ? AND playlists_songs.songId = songs.id AND songs.albumId = albums.id AND songs.singerId = singers.id";
        statement = connection.prepareStatement(songsSql);
        statement.setInt(1, res.getInt("id"));
        ResultSet songsResult = statement.executeQuery();
        while (songsResult.next() == true) {
            Song song = new Song(songsResult.getInt("id"), songsResult.getString("songs.name"),
                    songsResult.getInt("duration"), songsResult.getInt("year_released"),
                    songsResult.getString("singers.name"), songsResult.getString("albums.name"), songsResult.getString("url"));
            songs.add(song);
        }
        return songs;
    }
    /**
     * This function generate new playlist fron the DB.
     * 
     * @param res - (ResultSet)
     * @param songs - (List[song]) list of songs that create the playlist.
     *
     * @return p - (Playlist) 
     */
    private Playlist createPlaylistFromDB(ResultSet res, List<Song> songs) {
        Playlist p = null;
        try {
            String creator = res.getString("username");
            int id = res.getInt("id");
            String name = res.getString("name");
            Image img = convertBlobToImage(res.getBlob("image"));
            int popularity = res.getInt("popularity");
            boolean isAdmin = res.getBoolean("is_admin_made");
            p = new Playlist(id, creator, name, img, popularity, songs, isAdmin);
        } catch (SQLException e) {
        }
        return p;
    }
    /**
     * This function converte Blob into Image.
     * 
     * @param blob -(Blob) 
     * 
     * @return is - (Image) converted
     */
    private Image convertBlobToImage(Blob blob) {
        InputStream is = null;
        try {
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            is = new ByteArrayInputStream(blobAsBytes);
        } catch (SQLException e) {
        }
        return new Image(is);
    }
    /**
     * This function insert new playlist into te DB
     * 
     * @param name - (String) playlist name
     * @param blob - (ByteArrayInputStream) playlist image
     * @param songs - (list[Song]) list of songs
     * @param creatorId - (int) id number of the creator of the playlist
     * 
     * @return res - (boolean) true if success , false otherwish.
     */
    public boolean insertPlaylist(String name, ByteArrayInputStream blob, List<Song> songs, int creatorId) {
        boolean res;
        int newPlaylistId = insertPlaylistMetaData(name, blob, creatorId);
        if (newPlaylistId == -1) {
            return false;
        }
        res = insertPlaylistDetails(newPlaylistId, songs);
        return res;

    }
    /**
     * This function insert the mata data of the playlist
     * 
     * @param name - (String) playlist name
     * @param blob - (ByteArrayInputStream) playlist image
     * @param creatorId - (int) id number of the creator of the playlist
     * 
     * @return newPlaylistID - (int) the playlist new ID.
     */
    private int insertPlaylistMetaData(String name, ByteArrayInputStream blob, int creatorId) {
        Connection connection = null;
        PreparedStatement statement = null;
        int newPlaylistID = -1;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
            String sql = "INSERT INTO playlists(name,popularity,image,is_admin_made,creatorId) VALUES(?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, 0);
            statement.setBlob(3, blob);
            statement.setBoolean(4, false);
            statement.setInt(5, creatorId);
            statement.executeUpdate();
            // now extract the incremented ID of the new inserted playlist.
            sql = "SELECT id FROM playlists ORDER BY id DESC LIMIT 1";
            statement = connection.prepareStatement(sql);
            ResultSet res = statement.executeQuery();
            if (res.next() == true) {
                newPlaylistID = res.getInt("id"); // or 1 - first and only column.
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error on creating connection or query execution..." + ex.getMessage());
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
        return newPlaylistID;
    }
    /**
     * This function insert the playlist details
     * 
     * @param newPlaylistId - (int) he playlist new ID.
     * @param songs - (List[song]) list of songs that create the playlist.
     * 
     * @return true if success, and false otherwise.
     */
    private boolean insertPlaylistDetails(int newPlaylistId, List<Song> songs) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DEFAULT_JDBC_DRIVER);
            connection = DriverManager.getConnection(config.getDBUrl(), config.getUser(), config.getPassword());
            for (Song s : songs) { // a record for each song in the playlist.
                String sql = "INSERT INTO playlists_songs(playlistId, songId) VALUES(?,?)";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, newPlaylistId);
                statement.setInt(2, s.getSongId());
                statement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error on creating connection or query execution..." + ex.getMessage());
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
}