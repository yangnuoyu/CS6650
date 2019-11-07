import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

public class Operations {
  public static void skierPost(HttpServletRequest req, HttpServletResponse res, String urlPath,
                               String[] urlParts) throws IOException {

    final int resortId = Integer.parseInt(urlParts[1]);
    final int seasonId = Integer.parseInt(urlParts[3]);
    final int dayId = Integer.parseInt(urlParts[5]);
    final int skierId = Integer.parseInt(urlParts[7]);
    BufferedReader reader = req.getReader();
    String obj = reader.readLine();
    String[] objs = obj.split("[:,]");
    final int time = Integer.parseInt(objs[1]);
    final int liftID = Integer.parseInt(objs[3].substring(0, objs[3].length()-1));
    try {
      LiftRideDao liftRideDao = new LiftRideDao();
      liftRideDao.createLiftRide(new LiftRideServer(urlPath, resortId, seasonId, dayId, skierId, time, liftID));
      res.setStatus(HttpServletResponse.SC_CREATED);
    }
    catch (SQLException e) {
      e.printStackTrace();
      res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
  }

  public static void skierDayVerticalGet (HttpServletResponse res, String urlPath) throws IOException {
    try {
      LiftRideDao liftRideDao = new LiftRideDao();
      int result = liftRideDao.getLiftRide(urlPath);
      WebPrinter.print(res, result);
      res.setStatus(HttpServletResponse.SC_OK);
    } catch (SQLException e) {
      e.printStackTrace();
      res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
  }
}
