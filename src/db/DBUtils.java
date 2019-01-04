package db;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import tubit.models.ClientData;
import tubit.models.MakePlaylistModel;
import tubit.models.Playlist;
import tubit.models.PlaylistChooserModel.FILTER;
import tubit.models.Song;

public class DBUtils {

    private static DBUtils instance;
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:3306/tubitdb?useSSL=false";
    private final String USER = "root";
    private final String PASS = "alex1992";

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
    public synchronized ClientData checkClientInDB(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ClientData clientData = null;
        List<Playlist> playlists = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
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

    public synchronized void updatePlaylistPopularity(int playlistId, int change) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
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

    public synchronized void addFavoritePlaylistInDB(int clientId, int playlistId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
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

    public synchronized void removeFavoritePlaylistsDB(int clientId, int playlistId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
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
     *
     * @param username
     * @param email
     * @param password
     * @return
     */
    public synchronized boolean updateDB(String username, String email, String password) {
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

    public synchronized List<String> getAllGenres() {
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
    

    public List<Song> getSongsByCriteria(MakePlaylistModel.SEARCH_CRITERIA c, String searchField) {
        List<Song> selectedSongs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

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

    public synchronized List<Playlist> getPlaylists(boolean isAdmin, FILTER f) {
        List<Playlist> playlists = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
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

    public boolean insertPlaylist(String name, ByteArrayInputStream blob, List<Song> songs, int creatorId) {
        boolean res;
        int newPlaylistId = insertPlaylistMetaData(name, blob, creatorId);
        if (newPlaylistId == -1) {
            return false;
        }
        res = insertPlaylistDetails(newPlaylistId, songs);
        return res;

    }

    private int insertPlaylistMetaData(String name, ByteArrayInputStream blob, int creatorId) {
        Connection connection = null;
        PreparedStatement statement = null;
        int newPlaylistID = -1;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
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

    private boolean insertPlaylistDetails(int newPlaylistId, List<Song> songs) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
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
//Image img = new Image(ImageIO.read(new ByteArrayInputStream(
//playListResult.getBlob("playlistImg").getBytes(1, playListResult.getBlob("playlistImg").length()))))
