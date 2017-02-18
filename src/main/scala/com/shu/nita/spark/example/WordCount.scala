package com.shu.nita.spark.example

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by andy on 18/02/2017.
  */
object WordCount {
  def main(args: Array[String]) {
    val sparkConf=new SparkConf().setAppName("WordCount")
    val sc=new SparkContext(sparkConf)
    val textFile = sc.textFile("hdfs://192.168.3.105:9000/video/tout/part-00000")
    val counts = textFile.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
    counts.saveAsTextFile("hdfs://192.168.3.105:9000/video/tout/result")
  }
}
