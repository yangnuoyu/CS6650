import java.util.ArrayList;
import java.util.List;

public class LatencyAll {
  List<LatencyStatistics> endpointStats;
  public LatencyAll() {
    this.endpointStats = new ArrayList<>();
  }
  public void addStatistics(LatencyStatistics newStatistics) {
    this.endpointStats.add(newStatistics);
  }

  static class LatencyStatistics {

    String URL;
    String operation;
    double mean;
    long max;

    public LatencyStatistics(String URL, String operation, double mean, long max) {
      this.URL = URL;
      this.operation = operation;
      this.mean = mean;
      this.max = max;
    }
  }
}
