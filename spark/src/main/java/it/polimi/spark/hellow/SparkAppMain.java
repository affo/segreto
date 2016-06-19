package it.polimi.spark.hellow;

import org.apache.mesos.Protos;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.IOException;

/**
 * Created by Riccardo on 14/06/16.
 */
public class SparkAppMain {

        public static void main(String[] args) throws IOException{
                System.out.println("Hello World");

                SparkConf conf = new SparkConf().setAppName("Hello World");//.setMaster("local[*]");
                JavaSparkContext jsc = new JavaSparkContext(conf);

                JavaRDD<String> stringJavaRDD = jsc.textFile(args[0]);
                System.out.println("Number of lines in file = " + stringJavaRDD.count());

        }
}
