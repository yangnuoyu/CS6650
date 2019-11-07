import java.util.*;

public class RunTimeAnalysis {
  Queue<Long> data;
  int n;
  final int MAX_CAPACITY;
  long maxLatency;
  double meanLatency;

  public RunTimeAnalysis(int capacity) {
    this.MAX_CAPACITY = capacity;
    this.data = new LinkedList<>();
    this.n = 0;
    this.maxLatency = 0;
    this.meanLatency = 0;
  }

  public synchronized void addLatency(Long latency) {
    if (latency > this.maxLatency) this.maxLatency = latency;
    if (n == MAX_CAPACITY && !this.data.isEmpty()) {
      this.meanLatency -= (double)this.data.poll() / n;
      this.data.add(latency);
      this.meanLatency += (double)latency / n;
    } else {
      this.data.add(latency);
      this.meanLatency = this.meanLatency * ((double)n / (n+1)) + (double)latency / (n+1);
      n += 1;
    }
  }
}
