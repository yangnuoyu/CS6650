public class LiftRideServer {
  final int skierId;
  final int resortId;
  final int seasonId;
  final int dayId;
  final int time;
  final int liftID;
  public LiftRideServer(int skierId, int resortId, int seasonId, int dayId, int time, int liftID) {
    this.skierId = skierId;
    this.resortId = resortId;
    this.seasonId = seasonId;
    this.dayId = dayId;
    this.time = time;
    this.liftID = liftID;
  }

  public int getSkierId() {
    return this.skierId;
  }

  public int getResortId() {
    return this.resortId;
  }

  public int getSeasonId() {
    return this.seasonId;
  }

  public int getDayId() {
    return this.dayId;
  }

  public int getTime() {
    return this.time;
  }

  public int getLiftID() {
    return this.liftID;
  }
}
