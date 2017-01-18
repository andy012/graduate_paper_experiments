package com.shu.nita.ffmpeg.split;



import com.shu.nita.ffmpeg.exception.MadsnotepadException;
import com.shu.nita.ffmpeg.util.CmdExecute;
import com.shu.nita.hadoop.HadoopFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by gjh on 7/10/15.
 * 视频的分割和上传到HDFS
 * 通过传递两个参数来决定如何分割，1：集群的cpu核数，2：文件名字
 */
public class VideoSplitAndUploadToHDFS {
    public static void splitUpload(String filename, int cpuCores,String folderName){
        FFMPEGSplitVideo video=new FFMPEGSplitVideo();
        CmdExecute cmdExecute=new CmdExecute();
        video.setCmdExecute(cmdExecute);
        int sumTime= 0;
        try {
            sumTime = video.getAllTime(filename);
        int index=(sumTime/cpuCores==0)?sumTime/cpuCores:sumTime/cpuCores+1;
        System.out.println(index);
        int j=1;
        //分割视频文件
        for(int i=0;i<=sumTime;i+=index){
            String outputFilename=filename.substring(0,filename.indexOf("."))+j+filename.substring(filename.indexOf("."),filename.length());
            video.splitVideo(filename,outputFilename,i+"",index+"");
            j++;
        }
        //上传文件到HDFS
        HadoopFile t = new HadoopFile();
        t.DeleteDir("/data/abstract/"+folderName);
        t.MakeDir("/data/abstract/"+folderName);
//        for(int i=1;i<j;i++){
        for(int i=1;i<5;i++){
            String outputFilename=filename.substring(0,filename.indexOf("."))+i+filename.substring(filename.indexOf("."),filename.length());
            t.sendFile("hdfs://192.168.3.105:9000/data/abstract/"+folderName,outputFilename);
            File file =new File(outputFilename);
            file.delete();
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MadsnotepadException e) {
            e.printStackTrace();
        }
    }



    public static void splitUploadWithoutHadoopUpload(String filename, int cpuCores){
        FFMPEGSplitVideo video=new FFMPEGSplitVideo();
        CmdExecute cmdExecute=new CmdExecute();
        video.setCmdExecute(cmdExecute);
        int sumTime= 0;
        try {
            sumTime = video.getAllTime(filename);
            int index=(sumTime/cpuCores==0)?sumTime/cpuCores:sumTime/cpuCores+1;
            System.out.println(index);
            int j=1;
            //分割视频文件
            for(int i=0;i<=sumTime;i+=index){
                String outputFilename=filename.substring(0,filename.indexOf("."))+j+filename.substring(filename.indexOf("."),filename.length());
                video.splitVideo(filename,outputFilename,i+"",index+"");
                j++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MadsnotepadException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        //splitUploadWithoutHadoopUpload("/Users/andy/Desktop/video/LostInTranslation.avi",80);
        splitUpload("/Users/andy/Desktop/video/LostInTranslation.avi",20,"test1");
    }

}
