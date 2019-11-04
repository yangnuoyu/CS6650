import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
public class DataCache {
  private final int capacity;
  private BlockingQueue<LiftRideServer> cache;
  public DataCache(int capacity) {
    this.capacity = capacity;
    this.cache = new LinkedBlockingQueue<>(capacity);
  }

  public void addToCache (LiftRideServer newLiftRide) {
    try {
      cache.put(newLiftRide);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

//  public void writeToDataBase() {
//    LiftRideDao liftRideDao = new LiftRideDao();
//    try {
//      liftRideDao.createMultiLiftRide(this.cache, 1000);
//    } catch (SQLException | InterruptedException e) {
//      e.printStackTrace();
//    }
//  }
}
