import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.LinkedList;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

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
      res.setStatus(HttpServletResponse.SC_CREATED);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      Integer obj = 1;
      String jsonString = new Gson().toJson(obj);
      PrintWriter out = res.getWriter();
      res.setContentType("application/json");
      res.setCharacterEncoding("UTF-8");
      out.print(jsonString);
      out.flush();
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
    if (!isUrlValid_Get(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      res.setStatus(HttpServletResponse.SC_CREATED);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      //res.getWriter().write("It works!");
      Integer obj = 1;
      String jsonString = new Gson().toJson(obj);
      PrintWriter out = res.getWriter();
      res.setContentType("application/json");
      res.setCharacterEncoding("UTF-8");
      out.print(jsonString);
      out.flush();
    }
  }

  private boolean isUrlValid_Get(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    return urlNewLiftRide(urlPath) || urlTotalVertical(urlPath);
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
