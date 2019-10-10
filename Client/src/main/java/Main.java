public class Main {
  public static void main(String[] arg) throws Exception {
    //String basePath = "http://localhost:8080/lab02_war_exploded";
    String basePath = "http://ec2-3-95-25-236.compute-1.amazonaws.com:8080/lab02_war";
    IUpicResortsClient client = IUpicResortsClient.createClient(128, 20000,
            40, 20, basePath);
    client.open();
    client.analysis();
  }
}
