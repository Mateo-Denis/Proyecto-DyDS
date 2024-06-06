package utils;

import utils.wiki.RatedWikiPage;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

  private static final String URL = "jdbc:sqlite:./dictionary.db";
  private static final int QUERY_TIMEOUT = 30;

  public static void loadDatabase() {
    try (Connection connection = DriverManager.getConnection(URL)) {
      if (connection != null) {
        DatabaseMetaData meta = connection.getMetaData();
        System.out.println("The driver name is " + meta.getDriverName());

        createTable(connection, "catalog",
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT UNIQUE, " +
                        "extract TEXT, " +
                        "source INTEGER");

        createTable(connection, "rated_pages",
                "id INTEGER PRIMARY KEY, " +
                        "title TEXT, " +
                        "rating INTEGER, " +
                        "time TIMESTAMP");
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createTable(Connection connection, String tableName, String tableDefinition) {
    String createTableSQL = String.format("CREATE TABLE IF NOT EXISTS %s (%s);", tableName, tableDefinition);
    try (Statement statement = connection.createStatement()) {
      statement.setQueryTimeout(QUERY_TIMEOUT);  // set timeout to QUERY_TIMEOUT sec.
      statement.executeUpdate(createTableSQL);
      System.out.println("Table " + tableName + " is ready.");
    } catch (SQLException e) {
      System.out.println("Error creating table " + tableName + ": " + e.getMessage());
    }
  }

  public static ArrayList<String> getTitles() throws SQLException {
    ArrayList<String> titles = new ArrayList<>();
    Connection connection = DriverManager.getConnection(URL);
    String sql = "SELECT title FROM catalog";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ResultSet rs = preparedStatement.executeQuery();

    while (rs.next()) {
      titles.add(rs.getString("title"));
    }

    rs.close();
    preparedStatement.close();
    connection.close();

    return titles;
  }
  public static void saveInfo(String title, String extract) throws SQLException {
    Connection connection = DriverManager.getConnection(URL);
    Statement statement = connection.createStatement();
    statement.setQueryTimeout(QUERY_TIMEOUT);

    String sql = "REPLACE INTO catalog VALUES (NULL, ?, ?, 1)";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, title);
    preparedStatement.setString(2, extract);

    preparedStatement.executeUpdate();

    preparedStatement.close();
    statement.close();
    connection.close();
  }

  public static String getExtract(String title) throws SQLException {
    String url = "jdbc:sqlite:./dictionary.db";
    Connection connection = DriverManager.getConnection(url);
    PreparedStatement preparedStatement = connection.prepareStatement("SELECT extract FROM catalog WHERE title = ?");
    preparedStatement.setString(1, title);
    ResultSet rs = preparedStatement.executeQuery();

    if (rs.next()) {
      String result = rs.getString("extract");
      rs.close();
      preparedStatement.close();
      connection.close();
      return result;
    }else {
      rs.close();
      preparedStatement.close();
      connection.close();
      return null;
    }
  }

  public static int getRating(String id) throws SQLException {
    Connection connection = DriverManager.getConnection(URL);
    String sql = "SELECT rating FROM rated_pages WHERE id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, id);
    ResultSet rs = preparedStatement.executeQuery();

    if (rs.next()) {
      int result = rs.getInt("rating");
      rs.close();
      preparedStatement.close();
      connection.close();
      return result;
    } else {
      rs.close();
      preparedStatement.close();
      connection.close();
      return -1;
    }
  }

  public static void deleteEntry(String title) throws SQLException {
    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;

    connection = DriverManager.getConnection(URL);
    statement = connection.createStatement();
    statement.setQueryTimeout(QUERY_TIMEOUT);

    String sql = "DELETE FROM catalog WHERE title = ?";
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, title);
    preparedStatement.executeUpdate();

    if (preparedStatement != null) {
      preparedStatement.close();
    }
    if (statement != null) {
      statement.close();
    }
    if (connection != null) {
      connection.close();
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static void insertRatedPage(int id, String title, int rating, String timestamp) throws SQLException{
    String sql = "INSERT INTO rated_pages(id, title, rating, time) VALUES(?, ?, ?, ?)";

    Connection connection = DriverManager.getConnection(URL);
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setInt(1, id);
    pstmt.setString(2, title);
    pstmt.setInt(3, rating);
    pstmt.setString(4, timestamp);
    pstmt.executeUpdate();

    pstmt.close();
    connection.close();
  }
  public static void updateRatedPage(int id, String title, int rating, String timestamp) throws SQLException{
    String sql = "UPDATE rated_pages SET title = ?, rating = ?, time = ? WHERE id = ?";

    Connection connection = DriverManager.getConnection(URL);
    PreparedStatement pstmt = connection.prepareStatement(sql);

    pstmt.setString(1, title);
    pstmt.setInt(2, rating);
    pstmt.setString(3, timestamp);
    pstmt.setInt(4, id);
    pstmt.executeUpdate();

    pstmt.close();
    connection.close();
  }

    public static ArrayList<RatedWikiPage> getRatedPages() throws SQLException {
      ArrayList<RatedWikiPage> ratedPages = new ArrayList<>();
      Connection connection = DriverManager.getConnection(URL);
      String sql = "SELECT * FROM rated_pages";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        ratedPages.add(new RatedWikiPage(rs.getString("title")
                , rs.getString("id")
                , rs.getInt("rating")
                , rs.getTimestamp("time")));
      }

      rs.close();
      preparedStatement.close();
      connection.close();

      return ratedPages;
    }
}
