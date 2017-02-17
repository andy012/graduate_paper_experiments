package com.shu.nita.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.namenode.NameNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by andy on 08/01/2017.
 */
public class DataBlockInfoExperiment {
    private Configuration conf =null;

    public DataBlockInfoExperiment(){
        conf =new Configuration();
        conf.addResource(new Path("/Users/andy/Documents/github/andy012/experiments/src/main/resources/core-site.xml"));
    }

    public void getBlocks(String hdfsPath){
//        Path path=new Path(hdfsPath);
        try {
            DFSClient dfsClient = new DFSClient(conf);
            List<LocatedBlock> locatedBlockList = dfsClient.open(hdfsPath).getAllBlocks();
            for (LocatedBlock locatedBlock:locatedBlockList){
                System.out.println(locatedBlock.toString());

                System.out.println(locatedBlock.getBlock());
                System.out.println(locatedBlock.getLocations()[0].getName());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<LocatedBlock> getAllBlocks(String hdfsPath){
//        Path path=new Path(hdfsPath);
        List<LocatedBlock> locatedBlockList=null;
        try {
            DFSClient dfsClient = new DFSClient(conf);
            locatedBlockList = dfsClient.open(hdfsPath).getAllBlocks();
            for (LocatedBlock locatedBlock:locatedBlockList){
                System.out.println(locatedBlock.toString());

                System.out.println(locatedBlock.getBlock());
                System.out.println(locatedBlock.getLocations()[0].getName());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return locatedBlockList;
    }

    public void showBlock(){
        HashMap<String,List<String>> hashMap = new HashMap<String, List<String>>();
        for(int i=1;i<=20;++i){
            String path="/data/abstract/test2/LostInTranslation"+i+".avi";
            List<LocatedBlock> locatedBlockList=getAllBlocks(path);

            for (LocatedBlock locatedBlock:locatedBlockList){

                for(int j=0;j<3;++j){

                    if(hashMap.containsKey(locatedBlock.getLocations()[j].getName())){

                        hashMap.get(locatedBlock.getLocations()[j].getName()).add(path);

                    }else {
                        List<String> list=new LinkedList<String>();
                        hashMap.put(locatedBlock.getLocations()[j].getName(),list);
                        hashMap.get(locatedBlock.getLocations()[j].getName()).add(path);

                    }
                }
            }

        }

        for (String key:hashMap.keySet()){
            System.out.println(key);
            for (String str:hashMap.get(key)){
                System.out.println("\t"+str);
            }
        }

    }
    public void showBlock(String path){
        HashMap<String,List<String>> hashMap = new HashMap<String, List<String>>();

        List<LocatedBlock> locatedBlockList=getAllBlocks(path);

        for (LocatedBlock locatedBlock:locatedBlockList){

            for(int j=0;j<3;++j){

                if(hashMap.containsKey(locatedBlock.getLocations()[j].getName())){
                    hashMap.get(locatedBlock.getLocations()[j].getName()).add(locatedBlock.getBlock().toString());

                }else {
                    List<String> list=new LinkedList<String>();
                    hashMap.put(locatedBlock.getLocations()[j].getName(),list);
                    hashMap.get(locatedBlock.getLocations()[j].getName()).add(locatedBlock.getBlock().toString());

                }
            }
        }

        for (String key:hashMap.keySet()){
            System.out.println(key);
            for (String str:hashMap.get(key)){
                System.out.println("\t"+str);
            }
        }

    }

    public static void main(String[] args) {
        DataBlockInfoExperiment dataBlockInfoExperiment = new DataBlockInfoExperiment();
        //dataBlockInfoExperiment.getBlocks("/data/abstract/test2/LostInTranslation1.avi");

        dataBlockInfoExperiment.showBlock("/video/LostInTranslation2.avi");

    }
}
