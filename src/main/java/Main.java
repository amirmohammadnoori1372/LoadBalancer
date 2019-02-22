public class Main {
    public static void main(String[] args) {
        RandomUrls.generateUrls();
        LoadBalancer loadBalancer = LoadBalancer.getInstance();
        loadBalancer.handleRequest();
    }
}
