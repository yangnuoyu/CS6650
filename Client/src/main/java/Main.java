public class Main {
  public static void main(String[] arg) throws Exception {
    //Local
    //String basePath = "http://localhost:8080";

    //Virginia
    //String basePath = "http://ec2-3-95-25-236.compute-1.amazonaws.com:8080/lab02_war";

    //Oregon
    //String basePath = "http://ec2-34-210-132-72.us-west-2.compute.amazonaws.com:8080/lab02_war";

    //balancer
    //String basePath = "http://CS6650-0d80f865fd8f2288.elb.us-west-2.amazonaws.com:8080/lab02_war";

    // Google Cloud
    String basePath = "https://cs6650-259503.appspot.com";
    IUpicResortsClient client = IUpicResortsClient.createClient(Integer.parseInt(arg[0]), 20000,
            40, 20, basePath);
    client.open();
    client.analysis();
  }
}
