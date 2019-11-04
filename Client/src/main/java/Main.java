public class Main {
  public static void main(String[] arg) throws Exception {
    //Local
    String basePath = "http://localhost:8080/lab02_war_exploded";

    //Virginia
    //String basePath = "http://ec2-3-95-25-236.compute-1.amazonaws.com:8080/lab02_war";

    //Oregon
    //String basePath = "http://ec2-34-217-76-9.us-west-2.compute.amazonaws.com:8080/lab02_war";
    IUpicResortsClient client = IUpicResortsClient.createClient(256, 20000,
            40, 20, basePath);
    client.open();
    client.analysis();
  }
}
