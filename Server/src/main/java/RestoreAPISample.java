import io.swagger.client.*;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.ResortsApi;
import java.io.File;
import java.lang.reflect.Type;
import java.util.*;
public class RestoreAPISample {
  public static void main(String[] args) {
    String basePath = "http://localhost:8080/lab02_war_exploded";
    SkiersApi apiInstance = new SkiersApi();
    ApiClient client = apiInstance.getApiClient();
    client.setBasePath(basePath);
    try {
      ApiResponse response = apiInstance.writeNewLiftRideWithHttpInfo(new LiftRide(), 123,"123", "123", 123);
    } catch (ApiException e) {
      System.err.println("Exception when writing new lift ride!");
      e.printStackTrace();
    }
  }
}
