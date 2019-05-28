package sample;

import java.net.*;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label CPU;
    @FXML
    private Label CORES;
    @FXML
    private Label RAM;
    @FXML
    private Label bubble;
    public LineChart<String, Integer> lineChart;
    @FXML
    private Label primens;
    @FXML
    private Label floatpo;
   @FXML
   private Label MAC;
   @FXML
   private ProgressBar myprogressbar;
   @FXML
   private ProgressIndicator myprogressindicator;
   @FXML
   private Label parprimenumb;
    @FXML
    private Label singlecorescore;
    @FXML
    private Label multicorescore;
    @FXML
    private Label rank;
    XYChart.Series series2 = new XYChart.Series<>();
    XYChart.Series series1 = new XYChart.Series<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO Auto-generated method stu
        lineChart.getXAxis().setAutoRanging(true);
        lineChart.getYAxis().setAutoRanging(true);



        series2.getData().add(new XYChart.Data<>("The World", 1.5));
        lineChart.getData().add(series2);
        XYChart.Series series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Reference", 1));
        lineChart.getData().add(series);


    }

    Long Bubblesort(){
        myprogressbar.setProgress(0.25);
        myprogressindicator.setProgress(0.25);
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i=40000; i>0; --i){
            numbers.add(i);
        }
        Long time = java.lang.System.currentTimeMillis();
        int x;
        for (int i=0; i<40000; ++i){
            for (int y=i+1; y<40000; ++y){
                if (numbers.get(y) < numbers.get(i)){
                    x = numbers.get(i);
                    numbers.set(i, numbers.get(y));
                    numbers.set(y, x);
                }
            }
        }
        time-= java.lang.System.currentTimeMillis();
        time=time*-1;
        return time;
    }

    Long primenumberbench(){
        myprogressbar.setProgress(0.5);
        myprogressindicator.setProgress(0.5);
        boolean y = false;
        ArrayList<Integer> primenumb = new ArrayList<>();
        Long time = java.lang.System.currentTimeMillis();
        for (int i=2; i<100000; ++i){
            for (int j=2; j<i; ++j){
                if (i%j == 0){
                    y=true;
                }
            }
            if (!y){ primenumb.add(i); }
            y=false;
        }
        time-= java.lang.System.currentTimeMillis();
        time=time*-1;
        return time;
    }

    Long floatingpointcalculation(){
        myprogressbar.setProgress(0.75);
        myprogressindicator.setProgress(0.75);
        float a=12312767856785.34345f;
        float b=12341234.23452345234f;
        float x=232412341234.12341234134123412343f;
        float y=23410349813412434532.3f;
        float z=123434.4546456745657f;
        float r;
        Long time = java.lang.System.currentTimeMillis();
        for (int i=0; i<1000000000; ++i){
            r = a*x/(y*z)%b;
        }
        time-= java.lang.System.currentTimeMillis();
        time=time*-1;
        return time;
    }

    static String getmac() throws UnknownHostException, SocketException{
        String mymac="";
        try {
            InetAddress ip = InetAddress.getLocalHost();

            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();

                if(mac != null) {

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                        mymac+=String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");

                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }
        return (mymac.substring(0,17));
    }

    @FXML
    public void test(ActionEvent e) throws java.io.IOException{
        try {
            String CPUname = System.getenv("PROCESSOR_IDENTIFIER");
            String CPUcores = System.getenv("NUMBER_OF_PROCESSORS");
            String Mac = getmac();
            CPU.setText(CPUname);
            CORES.setText(CPUcores);
            long maxMemory = Runtime.getRuntime().maxMemory();
            maxMemory = maxMemory / 1000000000;
            RAM.setText("This app has available: " + Long.toString(maxMemory) + " gb");

            Long ETbubblesort = Bubblesort();
            Long ETprimens = primenumberbench();
            Long ETfloatcalculation = floatingpointcalculation();
            Long ETprimenspar = notmain.parallelprimenumbersearch();

            bubble.setText(ETbubblesort.toString());
            primens.setText(ETprimens.toString());
            floatpo.setText(ETfloatcalculation.toString());
            MAC.setText(Mac);
            parprimenumb.setText(ETprimenspar.toString());


            postData newPost= new postData(CPUname, CPUcores,Long.toString(maxMemory), ETbubblesort, ETfloatcalculation, ETprimens, ETprimenspar, Mac);
            newPost.post("http://10.81.14.68:5010/postSpecs");


            getResults newGet= new getResults(getmac());
            String [] words = newGet.getData("10.81.14.68:5010");


            singlecorescore.setText(words[0]);

            multicorescore.setText(words[1]);
            rank.setText(words[2]);

            series1.getData().add(new XYChart.Data<>("You", Float.valueOf(words[0])));
            lineChart.getData().add(series1);

            ArrayList <Double> myarray = new ArrayList<>();

            for (int i=0; i<10; ++i){
                myarray.add(Math.random()*5.0);
            }

            for (double x : myarray){
                series2.getData().add(new XYChart.Data<>("The World", x));
            }

            myprogressbar.setProgress(1);
            myprogressindicator.setProgress(1);

        }
        catch (Exception d) { System.out.println("An exception has occured");}
    }
}

class notmain {
    static final int MAX = 100000;
    static final int THREADS = Runtime.getRuntime().availableProcessors();
    static final int ARRINITSIZE = 100000;
    static ArrayList<Integer> primes = new ArrayList<Integer>(ARRINITSIZE);

    static Long parallelprimenumbersearch() throws InterruptedException{
        Long time = java.lang.System.currentTimeMillis();
        Thread[] t = new Thread[THREADS];
        PrimeRun.m = new Monitor();

        for (int i=0; i<THREADS; i++) {
            t[i] = new Thread(new PrimeRun(i) );
            t[i].start();
        }

        // wait for threads to finish
        for (int i=0; i<THREADS; i++)
            t[i].join();



        time-= java.lang.System.currentTimeMillis();
        time=time*-1;
        return time;
    }

    public static boolean isPrime(int n) {
        if (n == 2 || n == 3 || n == 5) return true;
        if (n <= 1 || (n&1) == 0) return false;

        for (int i = 3; i*i <= n; i += 2)
            if (n % i == 0) return false;

        return true;
    }

    public synchronized static void addPrime(int n) {
        primes.add(n);
    }

}

class PrimeRun implements Runnable {
    public static Monitor m;
    final int ID;
    public PrimeRun(int i) {
        ID = i;
    }

    public void run() {
        for(int i=0; i < notmain.MAX; i++) {
            if(i % notmain.THREADS == ID)
                if(notmain.isPrime(i))
                    m.addPrime(i);
        }
    }
}

class Monitor {
    public synchronized void addPrime(int n) {
        notmain.addPrime(n);
    }
}