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

    public static void main(String[] args) {
        DataBlockInfoExperiment dataBlockInfoExperiment = new DataBlockInfoExperiment();
        dataBlockInfoExperiment.getBlocks("/data/abstract/test2/LostInTranslation1.avi");
    }
}
