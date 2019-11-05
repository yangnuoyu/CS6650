public class RunTimeAnalysis {
  double meanLatency = 0;
  long maxLatency = 0;
  int n = 0;

  public synchronized void addLatency(Long latency) {
    this.n += 1;
    if (latency > this.maxLatency) this.maxLatency = latency;
    this.meanLatency = this.meanLatency * (1 - 1/(double)this.n) + (double)latency /this.n;
  }
}
