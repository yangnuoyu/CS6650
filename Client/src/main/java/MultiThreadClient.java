import org.apache.log4j.Logger;
import java.util.concurrent.*;

public class MultiThreadClient implements IUpicResortsClient{
  int numThreads;
  int numSkiers;
  int numLifts = 40;
  int numRuns = 10;
  String serverAddress;
  Logger errLogger;
  RunTimeAnalysis analysis;

  public MultiThreadClient(int numThreads, int numSkiers, String serverAddress) throws Exception{
    if (numThreads <= 0 || numThreads > this.MAX_NUM_THREADS)
      throw new WrongNumThreadsException("Threads number less than 1 or too large!");
    if (numSkiers <= 0 || numSkiers > this.MAX_NUM_SKIERS)
      throw new WrongNumSkiersException("Number of skiers is less than 1 or too large!");

    this.numThreads = numThreads;
    this.numSkiers = numSkiers;
    this.serverAddress = serverAddress;
    this.errLogger = Logger.getLogger(MultiThreadClient.class);
    this.analysis = new RunTimeAnalysis();
  }

  public MultiThreadClient(int numThreads, int numSkiers, int numLifts, int numRuns, String serverAddress)
          throws Exception{
    this(numThreads, numSkiers, serverAddress);
    if (numLifts < this.MIN_NUM_LIFTS || numLifts > this.MAX_NUM_LIFTS)
      throw new WrongNumLiftsException("Number of lifts is too small or too large!");
    if (numRuns <= 0 || numRuns > this.MAX_MEAN_PER_DAY)
      throw new WrongNumRunsException("Number of runs is less than 1 or too large!");
    this.numLifts = numLifts;
    this.numRuns = numRuns;
  }
  public void open() {
    int threadPhase1 = this.numThreads / 4;
    int threadPhase2 = this.numThreads;
    int threadPhase3 = this.numThreads / 4;

    int numPostPhase1 = (int)((numRuns*0.1)*((double)numSkiers/threadPhase1));
    int numPostPhase2 = (int)((numRuns*0.8)*((double)numSkiers/threadPhase2));
    int numPostPhase3 = (int)((numRuns*0.1)*((double)numSkiers/threadPhase3));

    CountDownLatch phase1Signal = new CountDownLatch(1);
    CountDownLatch phase2Signal = new CountDownLatch((int)(threadPhase1 * 0.1 ));
    CountDownLatch phase3Signal = new CountDownLatch((int)(threadPhase2 * 0.1 ));
    CountDownLatch endSignal = new CountDownLatch(threadPhase1 + threadPhase2 + threadPhase3);

    this.phase(threadPhase1, numPostPhase1, phase1Signal, phase2Signal, endSignal,
            1, 90, false);
    this.phase(threadPhase2, numPostPhase2, phase2Signal, phase3Signal, endSignal,
            91, 360, false);
    this.phase(threadPhase3, numPostPhase3, phase3Signal, null, endSignal,
            361, 420, true);

    this.analysis.start();
    phase1Signal.countDown();
    try {
      endSignal.await();
    } catch (InterruptedException e) {
      System.out.println("Current thread is interrupted while waiting!");
      e.printStackTrace();
    }
    this.analysis.stop();

    System.out.println("number of successful requests: "
            + this.analysis.numSuccess);
    System.out.println("number of unsuccessful requests: "
            + this.analysis.numNonSuccess);
    System.out.println("the total run time in milliseconds: "
            + (this.analysis.endTime - this.analysis.startTime));
  }

  @Override
  public void analysis() {
    this.analysis.analysis();
  }

  private void phase(int launchThreadNum, final int numPost, CountDownLatch wait,
                     CountDownLatch signal, CountDownLatch total,
                     int startTime, int endTime, boolean withGet) {
    Thread[] threads = new Thread[launchThreadNum];
    for (int i = 0; i < launchThreadNum; i++) {
      final int startID = i * numSkiers / launchThreadNum + 1;
      final int endID = (i + 1) * numSkiers / launchThreadNum;
      threads[i] = new ClientThread(this, numPost, wait, signal, total,
              startID, endID, startTime, endTime, withGet);
      threads[i].start();
    }
  }
}
