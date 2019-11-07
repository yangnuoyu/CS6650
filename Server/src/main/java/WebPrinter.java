import com.google.gson.Gson;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebPrinter {
  public static void print(HttpServletResponse res, Object result) throws IOException {
    String jsonString = new Gson().toJson(result);
    PrintWriter out = res.getWriter();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    out.print(jsonString);
    out.flush();
  }
}
