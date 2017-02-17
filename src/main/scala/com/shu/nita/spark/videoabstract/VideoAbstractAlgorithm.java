package com.shu.nita.spark.videoabstract;

import com.shu.nita.hadoop.HadoopFile;
import org.apache.hadoop.conf.Configuration;

import java.io.*;

/**
 * Created by andy on 18/01/2017.
 */
public class VideoAbstractAlgorithm {

    public static String process(String folder, String filename, String localFilePath)   {


        HadoopFile hadoopFile = new HadoopFile();

        try {
            long t1=System.currentTimeMillis();
            hadoopFile.downloadFile(folder + filename, localFilePath);
            long t2=System.currentTimeMillis();
            System.out.println("Download"+filename+":"+(t2-t1));
        }catch (Exception e){
            e.printStackTrace();
            return "error!"+folder+filename;
        }

//        Process process = null;
//        try {
//            long t1=System.currentTimeMillis();
//            process = Runtime.getRuntime().exec(new String[]{"/root/VideoAbstractApp",localFilePath+filename,localFilePath,"result"+filename});
//            //process = Runtime.getRuntime().exec(new String[]{"find","/","-name","root"});
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line=null;
//            while ((line=in.readLine())!=null){
//                System.out.println(line);
//            }
//            process.waitFor();
//            long t2=System.currentTimeMillis();
//            System.out.println("execute time:"+filename+(t2-t1));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        return "success!"+folder+filename;
    }

}
