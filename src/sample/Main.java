package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.gson.Gson;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


class JsonResponse {
    private int status;
    private String message;
    private ArrayList<Response> content;

    Response getResponse(){
        final Response response = content.get(0);
        return response;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

class Response{
    ArrayList<Double> otherCluster;
    Integer rank;
    Double selfResultMulti;
    Double selfResultNormal;

    public ArrayList<Double> getOtherCluster() {
        return otherCluster;
    }

    public Integer getRank() {
        return rank;
    }

    public Double getSelfResultMulti() {
        return selfResultMulti;
    }

    public Double getSelfResultNormal() {
        return selfResultNormal;
    }
}

class BenchmarksClass
{
    String CpuName;
    String Cores;
    String RamAllocated;
    Long ExecutionTimeBubble;
    Long ExecutionTimePrimeNumberSearch;
    Long ExecutionTimeFloatingPointOperation;
    Long ExecutionTimeParallelPrimeNumberSearch;
    String MacAddress;


    BenchmarksClass(String CpuName, String Cores, String RamAllocated, Long ExecutionTimeBubble, Long ExecutionTimeFloatingPointOperation, Long ExecutionTimePrimeNumberSearch, Long ExecutionTimeParallelPrimeNumberSearch, String MacAddress){
        this.CpuName= CpuName;
        this.Cores= Cores;
        this.RamAllocated= RamAllocated;
        this.ExecutionTimeBubble= ExecutionTimeBubble;
        this.ExecutionTimeParallelPrimeNumberSearch= ExecutionTimeParallelPrimeNumberSearch;
        this.ExecutionTimePrimeNumberSearch= ExecutionTimePrimeNumberSearch;
        this.ExecutionTimeFloatingPointOperation=ExecutionTimeFloatingPointOperation;
        this.MacAddress= MacAddress;
    }
}

class postData{

    BenchmarksClass benchmarks;
    postData(String CpuName, String Cores, String RamAllocated, Long ExecutionTimeBubble, Long ExecutionTimeFloatingPointOperation, Long ExecutionTimePrimeNumberSearch, Long ExecutionTimeParallelPrimeNumberSearch, String MacAddress){
        benchmarks = new BenchmarksClass(CpuName, Cores, RamAllocated, ExecutionTimeBubble, ExecutionTimeFloatingPointOperation, ExecutionTimePrimeNumberSearch, ExecutionTimeParallelPrimeNumberSearch, MacAddress);
    }

    void post(String url) throws UnsupportedEncodingException, IOException {
        Gson gson= new Gson();
        HttpPost post = new HttpPost(url);
        StringEntity  postingString = new StringEntity(gson.toJson(benchmarks));
        post.setEntity(postingString);
        post.setHeader("Content-type","application/json");
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse  response = httpClient.execute(post);
    }
}

class getResults {
    String MacAddress;

    getResults(String MacAddress) {
        this.MacAddress = MacAddress;
    }

    String[] getData(String url) throws URISyntaxException, IOException {
        System.out.println(1);
        URIBuilder builder = new URIBuilder();
        System.out.println(2);
        builder.setScheme("http").setHost(url).setPath("getNew").setParameter("MacAddress", MacAddress);
        System.out.println(3);
        URI uri = builder.build();
        System.out.println(4);
        HttpGet get = new HttpGet(uri);
        System.out.println(5);
        System.out.println(uri);
        HttpClient httpClient = new DefaultHttpClient();
        System.out.println(6);
        HttpResponse  response = httpClient.execute(get);
        System.out.println(7);
        System.out.println(response);
        BufferedReader rd = new BufferedReader
                (new InputStreamReader(
                        response.getEntity().getContent()));

        String line = rd.readLine();
        String[] words = line.split(" ");
        return words;
    }
}



    public class Main extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            primaryStage.setTitle("Benchmark");
            primaryStage.setScene(new Scene(root, 1250, 800));
            primaryStage.show();
        }


        public static void main(String[] args) {
            launch(args);
        }
    }
