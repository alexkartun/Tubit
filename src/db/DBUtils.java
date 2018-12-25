package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.util.Pair;
import tubit.models.Playlist;
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

    public List<Playlist> getAdmidPlaylists(List<String> genres, Map<String, Pair<String, Image>> moodMapper) {
        List<Playlist> adminPlaylists = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        for (String gen : genres) {
            try {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                // retrives all songs with given genre.
                String sql = "SELECT songs.songId, songs.songName, songs.songDuration, songs.yearReleased, singers.singerName, albums.albumName\n"
                        + "FROM songs, singers, albums\n"
                        + "WHERE singers.id = albums.singerId AND songs.albumId = albums.albumId and singers.genre = ?\n"
                        + "ORDER BY RAND() LIMIT 16";
                statement = connection.prepareStatement(sql);
                statement.setString(1, gen);
                ResultSet result = statement.executeQuery();
                // query excuted correctly
                List<Song> songs = new ArrayList<>();
                while (result.next() == true) {
                    Song song = new Song(result.getInt("songId"), result.getString("songName"),
                    result.getInt("songDuration"), result.getInt("yearReleased"),
                            result.getString("singerName"), result.getString("albumName"));
                    songs.add(song);
                }
                Pair<String, Image> mood = moodMapper.get(gen);
                adminPlaylists.add(new Playlist(mood.getKey(), mood.getValue(), songs));
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

        return adminPlaylists;
    }
}
