import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ResortsServlet")
public class ResortsServlet extends HttpServlet {
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("application/json");
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
      res.setStatus(HttpServletResponse.SC_CREATED);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      res.getWriter().write("It works!");
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    res.setContentType("json");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    System.out.println(urlPath);
//    if (urlPath == null || urlPath.isEmpty()) {
//      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//      res.getWriter().write("missing paramterers");
//      return;
//    }

    String[] urlParts = urlPath == null ? null : urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)
    if (!isUrlValid_Get(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      res.setStatus(HttpServletResponse.SC_CREATED);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      //res.getWriter().write("It works!");
      String jsonString = new Gson().toJson(1);
      PrintWriter out = res.getWriter();
      res.setContentType("application/json");
      res.setCharacterEncoding("UTF-8");
      out.print(jsonString);
      out.flush();
    }
  }

  private boolean isUrlValid_Get(String[] urlPath) {
    if (urlPath == null || urlPath.length == 0) return true;
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    return urlResort(urlPath) || urlSpecifyResort(urlPath);
  }

  private boolean isUrlValid_Post(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    return urlSpecifyResort(urlPath);
  }

  private boolean urlResort(String[] urlPath) {
    return urlPath.length == 0;
  }

  private boolean urlSpecifyResort(String[] urlPath) {
    if (urlPath.length != 3) return false;
    try {
      Integer.parseInt(urlPath[1]);
    } catch (NumberFormatException e) {
      return false;
    }
    return urlPath[2].equals("seasons");
  }

}
