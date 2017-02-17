package com.shu.nita.hadoop;

/**
 * Created by havstack on 3/26/15.
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.File;
import java.io.IOException;

public class HadoopFile {
    private Configuration conf =null;

    public HadoopFile(){
        conf =new Configuration();
        conf.addResource(new Path("core-site.xml"));

    }

    public HadoopFile(Configuration conf){
        this.conf =conf;
    }


    public boolean MakeDir(String p) {


        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            Path path = new Path(p);
            fs.mkdirs(path);
            fs.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean DeleteDir(String p) {


        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            Path path = new Path(p);
            fs.delete(path);
            fs.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendFile(String path, String localfile){
        File file=new File(localfile);
        if (!file.isFile()) {
            System.out.println(file.getName());
            return false;
        }
        try {
            FileSystem localFS = FileSystem.getLocal(conf);
            FileSystem hadoopFS = FileSystem.get(conf);
            Path hadPath=new Path(path);

            FSDataOutputStream fsOut=hadoopFS.create(new Path(path+"/"+file.getName()));
            FSDataInputStream fsIn=localFS.open(new Path(localfile));
            byte[] buf =new byte[1024];
            int readbytes=0;
            while ((readbytes=fsIn.read(buf))>0){
                fsOut.write(buf,0,readbytes);
            }
            fsIn.close();
            fsOut.close();

            FileStatus[] hadfiles= hadoopFS.listStatus(hadPath);
            for(FileStatus fs :hadfiles){
                System.out.println(fs.toString());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delFile(String hadfile){
        try {

            FileSystem hadoopFS = FileSystem.get(conf);
            Path hadPath=new Path(hadfile);
            Path p=hadPath.getParent();
            boolean rtnval= hadoopFS.delete(hadPath, true);

            FileStatus[] hadfiles= hadoopFS.listStatus(p);
            for(FileStatus fs :hadfiles){
                System.out.println(fs.toString());
            }
            return rtnval;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean downloadFile(String hadfile, String localPath){

        try {
            FileSystem localFS = FileSystem.getLocal(conf);
            FileSystem hadoopFS = FileSystem.get(conf);
            Path hadPath=new Path(hadfile);

            FSDataOutputStream fsOut=localFS.create(new Path(localPath+"/"+hadPath.getName()));
            FSDataInputStream fsIn=hadoopFS.open(hadPath);
            byte[] buf =new byte[1024*16];
            int readbytes=0;
            while ((readbytes=fsIn.read(buf))>0){
                fsOut.write(buf,0,readbytes);
            }
            fsIn.close();
            fsOut.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getFiles(Path path) throws IOException {
        FileSystem fs= FileSystem.get(conf);
        FileStatus[] fileStatus = fs.listStatus(path);
        int sumFile=0;
        for(int i=0;i<fileStatus.length;i++){
            if(fileStatus[i].isDir()){
                Path p = new Path(fileStatus[i].getPath().toString());
                getFiles(p);
            }else{
                sumFile++;
                //System.out.println(fileStatus[i].getPath().toString());
            }
        }
        return sumFile;
    }


    public long getFileSumSize(Path path) throws IOException {
        FileSystem fs= FileSystem.get(conf);
        FileStatus[] fileStatus = fs.listStatus(path);
        long sumFile=0;
        for(int i=0;i<fileStatus.length;i++){
            if(fileStatus[i].isDir()){
                Path p = new Path(fileStatus[i].getPath().toString());
                getFiles(p);
            }else{
                sumFile+=fileStatus[i].getLen();
                //System.out.println(fileStatus[i].getPath().toString());
            }
        }
        return sumFile;
    }


    public static void main(String[] args){
        HadoopFile t = new HadoopFile();
        t.downloadFile("/data/abstract/test1/LostInTranslation1.avi","/Users/andy/Documents/github/andy012/experiments/");
        //System.out.println(t.DeleteDir("/data/h264"));
        //t.MakeDir("/data/h264");
//        Path path=new Path("hdfs://sparknode4:9000/data/abstract");
//        try {
////            int file=t.getFiles(path);
////            System.out.println(file);
////            long size=t.getFileSumSize(path);
////            System.out.println(size);
//            t.downloadFile(path.toString()+"/videos1.avi","/home/havstack/videoAbstract/");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //t.MakeDir("/data/h264");
        //t.downloadFile("hdfs://192.168.8.220:9000/data/h264/video1.264","file:///home/havstack/Downloads");
        //t.sendFile("hdfs://slave2:9000/data/h264","/home/havstack/forhadoopFile/forinput/video1split0.264");
    }
}