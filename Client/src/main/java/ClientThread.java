import io.swagger.client.ApiClient;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class ClientThread extends Thread {
  private final int startID;
  private final int endID;
  private final int startTime;
  private final int endTime;
  private MultiThreadClient client;
  private final int numPost;
  private CountDownLatch wait;
  private CountDownLatch signal;
  private CountDownLatch total;
  private final boolean withGet;

  public ClientThread(MultiThreadClient client, int numPost, CountDownLatch wait,
                      CountDownLatch signal, CountDownLatch total,
                      int startID, int endID, int startTime, int endTime,
                      boolean withGet) {
    this.client = client;
    this.numPost = numPost;
    this.wait = wait;
    this.signal = signal;
    this.total = total;
    this.startID = startID;
    this.endID = endID;
    this.startTime = startTime;
    this.endTime = endTime;
    this.withGet = withGet;
  }

  @Override
  public void run() {
    try {
      this.wait.await();
      SkiersApi apiInstance = new SkiersApi();
      ApiClient client = apiInstance.getApiClient();
      client.setBasePath(this.client.serverAddress);
      ThreadLocalRandom random = ThreadLocalRandom.current();
      int success = 0;
      List<Long> timeInThread = new ArrayList<>();
      StringBuilder threadLogWriter = new StringBuilder();
      for (int i = 0; i < this.numPost; i++) {
        int resortID = 1;
        int skierID = random.nextInt(startID, endID+1);
        int dayID = 1;//random.nextInt(365);
        int seasonID = 2018;
        int liftID = random.nextInt(this.client.numLifts);
        int time = random.nextInt(startTime, endTime+1);
        long start = System.currentTimeMillis();
        ApiResponse responsePost = SkierActions.skierPost(apiInstance, resortID, seasonID, dayID,skierID,time, liftID);
        long end = System.currentTimeMillis();
        timeInThread.add(end - start);
        this.addLog(threadLogWriter, start, "POST", end - start, responsePost);
        if (withGet) {
          ApiResponse responseGet = SkierActions.skierGet(apiInstance, resortID, seasonID, dayID, skierID);
        }

        if (responsePost == null) continue;
        if(responsePost.getStatusCode() == 201) {
          // send post
          success += 1;
        } else {
          if (responsePost.getStatusCode() >= 400) {
            this.client.errLogger.error("Error: Http Code " + responsePost.getStatusCode());
          }
        }

      }
      this.client.analysis.addSuccess(success);
      this.client.analysis.addNonSuccess(this.numPost - success);
      this.client.analysis.addTime(timeInThread);
      threadLogWriter.deleteCharAt(threadLogWriter.length()-1);
      this.client.analysis.addLog(threadLogWriter.toString());
      if (signal != null) signal.countDown();
      total.countDown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  private void addLog(StringBuilder builder, long startTime, String requestType, long latency, ApiResponse response) {
    builder.append(startTime).append(",")
            .append(requestType).append(",")
            .append(latency).append(",");
    if (response == null) builder.append(-1);
    else builder.append(response.getStatusCode());
    builder.append("\n");
  }
}
