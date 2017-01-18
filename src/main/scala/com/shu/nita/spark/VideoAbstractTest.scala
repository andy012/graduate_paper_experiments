package com.shu.nita.spark

import org.apache.hadoop.fs.Path
import com.shu.nita.spark.videoabstract.{VideoAbstractAlgorithm, VideoInputFormat}
import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.io.Text
import scala.io.Source
/**
  * Created by andy on 18/01/2017.
  */
object VideoAbstractTest {
  def main(args: Array[String]) {




    val NUM_SAMPLES=10000
    val sparkConf=new SparkConf().setAppName("Pi")
    val sc=new SparkContext(sparkConf)

    val hadoopConfiguration=new  Configuration()
    hadoopConfiguration.addResource(new Path("core-site.xml"))

    val file = sc.newAPIHadoopFile(args(1), classOf[VideoInputFormat], classOf[Text], classOf[Text], hadoopConfiguration).
      map(key=>VideoAbstractAlgorithm.process(args(2),key._1.toString,args(3))).foreach(println)


    sc.stop();
  }
}
