import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {
  private static final int CAPACITY_STATISTICS = 400000;
  private RunTimeAnalysis postAnalysis;
  private RunTimeAnalysis getAnalysis;
  @Override
  public void init() throws ServletException {
    super.init();
    this.postAnalysis = new RunTimeAnalysis(CAPACITY_STATISTICS);
    this.getAnalysis = new RunTimeAnalysis(CAPACITY_STATISTICS);
  }


  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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
    if (!isUrlValid_Post(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      long start = System.currentTimeMillis();
      Operations.skierPost(req, res, urlPath, urlParts);
      long end = System.currentTimeMillis();
      this.postAnalysis.addLatency(end - start);
      this.getServletConfig().getServletContext().setAttribute("skierPostMean", this.postAnalysis.meanLatency);
      this.getServletConfig().getServletContext().setAttribute("skierPostMax", this.postAnalysis.maxLatency);
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
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
      long start = System.currentTimeMillis();
      Operations.skierDayVerticalGet(res,urlPath);
      long end = System.currentTimeMillis();
      this.getAnalysis.addLatency(end - start);
      this.getServletConfig()
              .getServletContext()
              .setAttribute("skierVerticalGetMean", this.getAnalysis.meanLatency);
      this.getServletConfig()
              .getServletContext()
              .setAttribute("skierVerticalGetMax", this.getAnalysis.maxLatency);

    } else if (urlTotalVertical(urlParts)) {
      final int skierId = Integer.parseInt(urlParts[1]);
      try {
        LiftRideDao liftRideDao = new LiftRideDao();
        String result = liftRideDao.getVertical(skierId);
        WebPrinter.print(res, result);
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
