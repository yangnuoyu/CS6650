import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

public class SkierActions {
  public static ApiResponse skierPost(SkiersApi apiInstance, int resortID, int seasonID,
                               int dayID, int skierID, int time, int liftId) {
    ApiResponse response = null;
    try {
      LiftRide liftRide = new LiftRide();
      liftRide.setLiftID(liftId);
      liftRide.setTime(time);
      response = apiInstance.writeNewLiftRideWithHttpInfo(liftRide,
              resortID, String.valueOf(seasonID), String.valueOf(dayID), skierID);
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return response;
  }

  public static ApiResponse skierGet(SkiersApi apiInstance, int resortID, int seasonID,
                              int dayID, int skierID) {
    ApiResponse response = null;
    try {
      response = apiInstance.getSkierDayVerticalWithHttpInfo(
              resortID,String.valueOf(seasonID),String.valueOf(dayID),skierID);
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return response;
  }
}
