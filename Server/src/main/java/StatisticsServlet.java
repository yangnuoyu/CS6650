import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SkierServlet")
public class StatisticsServlet extends HttpServlet{

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String urlPath = req.getPathInfo();
    if (urlPath != null) {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    LatencyAll output = new LatencyAll();
    if (this.getServletConfig().getServletContext().getAttribute("skierPostMax") != null) {
      String url = "/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}";
      long max = (long) this.getServletConfig().getServletContext().getAttribute("skierPostMax");
      double mean = (double) this.getServletConfig().getServletContext().getAttribute("skierPostMean");
      output.addStatistics(new LatencyAll.LatencyStatistics(url, "POST", mean, max));
    }
    if (this.getServletConfig().getServletContext().getAttribute("skierVerticalGetMax") != null) {
      String url = "/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}";
      long max = (long) this.getServletConfig().getServletContext().getAttribute("skierVerticalGetMax");
      double mean = (double) this.getServletConfig().getServletContext().getAttribute("skierVerticalGetMean");
      output.addStatistics(new LatencyAll.LatencyStatistics(url, "GET", mean, max));
    }
    WebPrinter.print(resp, output);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    this.doGet(req, resp);
  }
}
