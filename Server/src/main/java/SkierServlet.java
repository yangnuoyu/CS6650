import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {
  RunTimeAnalysis analysis = new RunTimeAnalysis();
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    //res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    Deque<Integer> dq = new LinkedList<>();
    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)
    if (!isUrlValid_Post(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      final int resortId = Integer.parseInt(urlParts[1]);
      final int seasonId = Integer.parseInt(urlParts[3]);
      final int dayId = Integer.parseInt(urlParts[5]);
      final int skierId = Integer.parseInt(urlParts[7]);
      BufferedReader reader = req.getReader();
      String obj = reader.readLine();
      String[] objs = obj.split("[:,]");
      final int time = Integer.parseInt(objs[1]);
      final int liftID = Integer.parseInt(objs[3].substring(0, objs[3].length()-1));

      this.analysis.start();
      try {
        LiftRideDao liftRideDao = new LiftRideDao();
        liftRideDao.createLiftRide(new LiftRideServer(skierId, resortId, seasonId, dayId, time, liftID));
        res.setStatus(HttpServletResponse.SC_CREATED);
      }
      catch (SQLException e) {
        e.printStackTrace();
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      }
      this.analysis.stop();
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    //res.setContentType("application/json");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)
    if (urlNewLiftRide(urlParts)) {
      final int resortId = Integer.parseInt(urlParts[1]);
      final int seasonId = Integer.parseInt(urlParts[3]);
      final int dayId = Integer.parseInt(urlParts[5]);
      final int skierId = Integer.parseInt(urlParts[7]);
      try {
        LiftRideDao liftRideDao = new LiftRideDao();
        int result = liftRideDao.getLiftRide(resortId,seasonId,dayId,skierId);
        String jsonString = new Gson().toJson(result);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
        res.setStatus(HttpServletResponse.SC_OK);
      } catch (SQLException e) {
        e.printStackTrace();
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      }
    } else if (urlTotalVertical(urlParts)) {
      final int skierId = Integer.parseInt(urlParts[1]);
      try {
        LiftRideDao liftRideDao = new LiftRideDao();
        String result = liftRideDao.getVertical(skierId);
        String jsonString = new Gson().toJson(result);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
        res.setStatus(HttpServletResponse.SC_OK);
      } catch (SQLException e) {
        e.printStackTrace();
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      }
    } else {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
  private boolean isUrlValid_Post(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    return urlNewLiftRide(urlPath);
  }

  private boolean urlNewLiftRide(String[] urlPath) {
    if (urlPath.length != 8) return false;
    if (!urlPath[2].equals("seasons")) return false;
    if (!urlPath[4].equals("days")) return false;
    if (!urlPath[6].equals("skiers")) return false;
    try {
      Integer.parseInt(urlPath[1]);
      Integer.parseInt(urlPath[3]);
      Integer.parseInt(urlPath[5]);
      Integer.parseInt(urlPath[7]);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private boolean urlTotalVertical(String[] urlPath) {
    if (urlPath.length != 3) return false;
    try {
      Integer.parseInt(urlPath[1]);
    } catch (NumberFormatException e) {
      return false;
    }
    return urlPath[2].equals("vertical");
  }

}
