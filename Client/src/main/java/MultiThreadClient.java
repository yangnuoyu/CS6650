import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MultiThreadClient implements IUpicResortsClient{
  private int numThreads;
  private int numSkiers;
  private int numLifts = 40;
  private int numRuns = 10;
  private String serverAddress;

  private int numSuccess;
  private int numNonSuccess;
  private Logger errLogger;
  private Long startTime;
  private Long endTime;
  private Vector<Long> time;
  private ConcurrentLinkedQueue<String> log;

  public MultiThreadClient(int numThreads, int numSkiers, String serverAddress) throws Exception{
    if (numThreads <= 0 || numThreads > this.MAX_NUM_THREADS)
      throw new WrongNumThreadsException("Threads number less than 1 or too large!");
    if (numSkiers <= 0 || numSkiers > this.MAX_NUM_SKIERS)
      throw new WrongNumSkiersException("Number of skiers is less than 1 or too large!");

    this.numThreads = numThreads;
    this.numSkiers = numSkiers;
    this.serverAddress = serverAddress;
    this.numSuccess = 0;
    this.numNonSuccess = 0;
    this.startTime = null;
    this.endTime = null;
    this.log = new ConcurrentLinkedQueue<>();
    this.time = new Vector<>();
    this.errLogger = Logger.getLogger(MultiThreadClient.class);
  }

  public MultiThreadClient(int numThreads, int numSkiers, int numLifts, int numRuns, String serverAddress)
          throws Exception{
    this(numThreads, numSkiers, serverAddress);
    if (numLifts < this.MIN_NUM_LIFTS || numLifts > this.MAX_NUM_LIFTS)
      throw new WrotngNumLiftsException("Number of lifts is too small or too large!");
    if (numRuns <= 0 || numRuns > this.MAX_MEAN_PER_DAY)
      throw new WrongNumRunsException("Number of runs is less than 1 or too large!");
    this.numLifts = numLifts;
    this.numRuns = numRuns;
  }
  private synchronized void addNumSuccess() {
    this.numSuccess += 1;
  }

  private synchronized void addNumNonSuccess() {
    this.numNonSuccess += 1;
  }

  private void addLog(long startTime, String requestType, long latency, int responseCode) {
    this.log.add(startTime + "," + requestType + "," + latency + "," + responseCode);
  }


  public void open() {
    int threadPhase1 = this.numThreads / 4;
    int threadPhase2 = this.numThreads;
    int threadPhase3 = this.numThreads / 4;

    int numPostPhase1 = (int)(numRuns*0.1)*(numSkiers/threadPhase1);
    int numPostPhase2 = (int)(numRuns*0.8)*(numSkiers/threadPhase2);
    int numPostPhase3 = (int)(numRuns*0.1)*threadPhase3;

    CountDownLatch phase1Signal = new CountDownLatch(1);
    CountDownLatch phase2Signal = new CountDownLatch((int)(threadPhase1 * 0.1 + 0.5));
    CountDownLatch phase3Signal = new CountDownLatch((int)(threadPhase2 * 0.1 + 0.5));
    CountDownLatch endSignal = new CountDownLatch(threadPhase1 + threadPhase2 + threadPhase3);

    this.phase(threadPhase1, numPostPhase1, phase1Signal, phase2Signal, endSignal,1, 90);
    this.phase(threadPhase2, numPostPhase2, phase2Signal, phase3Signal, endSignal,91, 360);
    this.phase(threadPhase3, numPostPhase3, phase3Signal, null, endSignal,361, 420);

    this.startTime = System.currentTimeMillis();
    phase1Signal.countDown();
    try {
      endSignal.await();
    } catch (InterruptedException e) {
      System.out.println("Current thread is interrupted while waiting!");
      e.printStackTrace();
    }
    this.endTime = System.currentTimeMillis();

    System.out.println("number of successful requests: " + this.numSuccess);
    System.out.println("number of unsuccessful requests: " + this.numNonSuccess);
    System.out.println("the total run time in milliseconds: " + (this.endTime - this.startTime));
  }

  public void analysis () {
    int n = this.time.size();
    this.time.sort(Comparator.naturalOrder());
    double median = this.time.get(n / 2);
    if (n % 2 == 0) median = (median + this.time.get(n / 2 - 1)) / 2;
    double mean = 0.0;
    for (int i = 0; i < n; i++) mean += this.time.get(i);
    mean /= n;
    double throughput =(double)(this.numSuccess + this.numNonSuccess) / (this.endTime - this.startTime);
    long p99 = this.time.get((n-1) * 99 / 100);
    long max = this.time.get(n-1);
    System.out.println("mean: " + mean + " median: " + median +
            " throughput: " + throughput + " p99: " + p99 + " max: " + max);
    File file = new File("log.csv");
    try {
      // create FileWriter object with file as parameter
      FileOutputStream outputStream = new FileOutputStream(file);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
      while (!log.isEmpty()) {
        writer.write(log.poll());
        writer.newLine();
      }
      writer.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void phase(int launchThreadNum, final int numPost, CountDownLatch wait,
                     CountDownLatch signal, CountDownLatch total, int startTime, int endTime) {
    Thread[] threads = new Thread[launchThreadNum];
    for (int i = 0; i < launchThreadNum; i++) {
      final int startID = i * numSkiers / launchThreadNum + 1;
      final int endID = (i + 1) * numSkiers / launchThreadNum;
      threads[i] = new Thread(() -> {
        try {
          wait.await();
          SkiersApi apiInstance = new SkiersApi();
          ApiClient client = apiInstance.getApiClient();
          client.setBasePath(serverAddress);
          ThreadLocalRandom random = ThreadLocalRandom.current();
          for (int i1 = 0; i1 < numPost; i1++) {
            int liftID = random.nextInt(endID - startID + 1) + startID;
            long start = System.currentTimeMillis();
            ApiResponse response = null;
            try {
              response = apiInstance.writeNewLiftRideWithHttpInfo(new LiftRide(),
                      123,"123", "123", 123);
            } catch (ApiException e) {
              System.err.println("Exception when writing new lift ride!");
              e.printStackTrace();
            }

            long end = System.currentTimeMillis();
            time.add(end - start);
            addLog(start, "POST", end - start, response.getStatusCode());
            if(response.getStatusCode() == 201) {
              // send post
              addNumSuccess();
            } else {

              addNumNonSuccess();
              if (response.getStatusCode() >= 400) {
                errLogger.error("Error: Http Code " + response.getStatusCode());
              }
            }
          }
          if (signal != null) signal.countDown();
          total.countDown();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      threads[i].start();
    }
  }
}
