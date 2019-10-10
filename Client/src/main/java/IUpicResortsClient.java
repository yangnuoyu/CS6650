/**
 * Interface of UpicResortsClient
 */
public interface IUpicResortsClient {

  int MAX_NUM_THREADS = 2048;
  int MAX_NUM_SKIERS = 50000;
  int MIN_NUM_LIFTS = 4;
  int MAX_NUM_LIFTS = 60;
  int MAX_MEAN_PER_DAY = 20;

  /**
   * Create new UpicResortClient.
   * @param numThreads number of treads you want to use to deal with the data.
   * @param numSkiers number of skiers in the resorts
   * @param serverAddress remote server address
   * @return new UpicResortClient
   * @throws Exception if the parameter provided is not illegal
   */
  static IUpicResortsClient createClient(int numThreads, int numSkiers, String serverAddress)
          throws Exception{
    return new MultiThreadClient(numThreads, numSkiers, serverAddress);
  }

  /**
   * Create new UpicResortClient.
   * @param numThreads number of treads you want to use to deal with the data.
   * @param numSkiers number of skiers in the resorts
   * @param numLifts number of lifts in the resorts
   * @param numRuns mean numbers of ski lifts each skier rides each day
   * @param serverAddress remote server address
   * @return new UpicResortClient
   * @throws Exception if the parameter provided is not illegal
   */
  static IUpicResortsClient createClient(int numThreads, int numSkiers, int numLifts, int numRuns,
                           String serverAddress) throws Exception {
    return new MultiThreadClient(numThreads, numSkiers, numLifts, numRuns, serverAddress);
  }

  /**
   * Open and run the client.
   */
  void open();

  /**
   * Statistic values of latency and log record.
   */
  void analysis();
}
