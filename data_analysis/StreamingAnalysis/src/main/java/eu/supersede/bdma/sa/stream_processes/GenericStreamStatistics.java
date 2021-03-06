package eu.supersede.bdma.sa.stream_processes;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import eu.supersede.bdma.sa.utils.Sockets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.TaskContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.OffsetRange;
import scala.Tuple2;

import java.util.Map;

/**
 * Created by snadal on 28/05/17.
 */
public class GenericStreamStatistics {

    public static void process(JavaInputDStream<ConsumerRecord<String, String>> kafkaStream, Broadcast<Map<String, Tuple2<Boolean,String>>> broadcastReleases) {
        Map<String, Map<String, Integer>> general_statistics = Maps.newConcurrentMap();
        broadcastReleases.value().forEach((s, booleanStringTuple2) -> {
            general_statistics.put(s,Maps.newHashMap());
        });

        // # of events in the last 5 min per release, updated every 10 sec
        /*JavaPairDStream<String,Integer> countsPerRelease = */
        kafkaStream.mapToPair(record ->new Tuple2<String,String>(record.topic(),record.value()))
                .groupByKey()
                .mapToPair(f -> new Tuple2<String,Integer>(f._1(), Iterables.size(f._2())))
                .reduceByKeyAndWindow((v1, v2) -> v1+v2, (v1, v2) -> v1-v2, new Duration(300000), new Duration(10000))//;
        /*countsPerRelease*/.foreachRDD((v1, v2) -> {
            try {
                Sockets.sendSocketAlert((new Gson().toJson(v1.collectAsMap())),"events_in_last_5_min");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
