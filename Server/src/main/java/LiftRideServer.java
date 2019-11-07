class LiftRideServer {
  final String url;
  final int skierId;
  final int resortId;
  final int seasonId;
  final int dayId;
  final int time;
  final int liftID;
  LiftRideServer(String url, int resortId, int seasonId, int dayId, int skierId, int time, int liftID) {
    this.url = url;
    this.resortId = resortId;
    this.seasonId = seasonId;
    this.dayId = dayId;
    this.skierId = skierId;
    this.time = time;
    this.liftID = liftID;
  }

  public String getUrl() {
    return this.url;
  }

  int getSkierId() {
    return this.skierId;
  }

  int getResortId() {
    return this.resortId;
  }

  int getSeasonId() {
    return this.seasonId;
  }

  int getDayId() {
    return this.dayId;
  }

  int getTime() {
    return this.time;
  }

  int getLiftID() {
    return this.liftID;
  }
}
