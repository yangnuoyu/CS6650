import java.sql.SQLException;

public class MySQLDemo {
  public static void main(String[] args) {
    LiftRideDao liftRideDao = new LiftRideDao();
    try {
      liftRideDao.createLiftRide(new LiftRideServer(10, 2, 3, 5, 500, 20));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
