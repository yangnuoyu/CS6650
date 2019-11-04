import java.sql.*;
import java.util.concurrent.BlockingQueue;

public class LiftRideDao {
  private Connection conn;
  private PreparedStatement preparedStatement;

  public LiftRideDao() {
    try {
      conn = DBCPDataSource.getConnection();
      preparedStatement = null;
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public void createLiftRide(LiftRideServer newLiftRide) throws SQLException {
    String insertQueryStatement = "INSERT IGNORE INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId)" +
            " VALUES (?,?,?,?,?,?)";
    preparedStatement = conn.prepareStatement(insertQueryStatement);
    preparedStatement.setInt(1, newLiftRide.getSkierId());
    preparedStatement.setInt(2, newLiftRide.getResortId());
    preparedStatement.setInt(3, newLiftRide.getSeasonId());
    preparedStatement.setInt(4, newLiftRide.getDayId());
    preparedStatement.setInt(5, newLiftRide.getTime());
    preparedStatement.setInt(6, newLiftRide.getLiftID());

    // execute insert SQL statement
    preparedStatement.executeUpdate();

    insertQueryStatement = "INSERT INTO Verticals (resortId, seasonId, dayId, skierId, vertical)" +
            " values(?,?,?,?,?)" +
            " ON DUPLICATE KEY UPDATE vertical = vertical + values(vertical)";
    preparedStatement = conn.prepareStatement(insertQueryStatement);

    preparedStatement.setInt(1, newLiftRide.getResortId());
    preparedStatement.setInt(2, newLiftRide.getSeasonId());
    preparedStatement.setInt(3, newLiftRide.getDayId());
    preparedStatement.setInt(4, newLiftRide.getSkierId());
    preparedStatement.setInt(5, newLiftRide.getLiftID() * 10);
    preparedStatement.executeUpdate();
    preparedStatement.close();
    conn.close();
  }

  public int getLiftRide(int resortId, int seasonId, int dayId, int skierId) throws SQLException {
    String selectQueryStatement = "SELECT vertical FROM Verticals" +
            " WHERE resortId=? and seasonId=? and dayId=? and skierId =?;";
    preparedStatement = conn.prepareStatement(selectQueryStatement);
    preparedStatement.setInt(1, resortId);
    preparedStatement.setInt(2, seasonId);
    preparedStatement.setInt(3, dayId);
    preparedStatement.setInt(4, skierId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    int output = resultSet.getInt("vertical");
    preparedStatement.close();
    conn.close();
    return output;
  }

  public String getVertical(int skierId) throws SQLException {
    String selectQueryStatement = "SELECT skierId, SUM(liftId * 10) as vertical" +
            "FROM LiftRides " +
            "WHERE skierId=?" +
            "GROUP BY skierId;";
    preparedStatement = conn.prepareStatement(selectQueryStatement);
    ResultSet resultSet = preparedStatement.executeQuery();
    StringBuilder builder = new StringBuilder("{\"Vertical\": [");
    resultSet.next();
    builder.append(String.format("{\"skierId\": %d,\"vertical\": %d}",
            skierId, resultSet.getInt("vertical")));
    builder.append("]}");
    preparedStatement.close();
    conn.close();
    return builder.toString();
  }

//  public void createMultiLiftRide(BlockingQueue<LiftRideServer> queue, int times) throws SQLException, InterruptedException {
//    String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId)" +
//            " VALUES (?,?,?,?,?,?)";
//    preparedStatement = conn.prepareStatement(insertQueryStatement);
//    for (int i = 0; i < times; i++) {
//      LiftRideServer newLiftRide = queue.take();
//      preparedStatement.setInt(1, newLiftRide.getSkierId());
//      preparedStatement.setInt(2, newLiftRide.getResortId());
//      preparedStatement.setInt(3, newLiftRide.getSeasonId());
//      preparedStatement.setInt(4, newLiftRide.getDayId());
//      preparedStatement.setInt(5, newLiftRide.getTime());
//      preparedStatement.setInt(6, newLiftRide.getLiftID());
//      preparedStatement.addBatch();
//    }
//    preparedStatement.executeBatch();
//    preparedStatement.close();
//    conn.close();
//  }
}