package com.flink.example;


import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.fs.DateTimeBucketer;
import org.apache.flink.streaming.connectors.fs.RollingSink;
import org.apache.flink.streaming.connectors.fs.StringWriter;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.Properties;

public class ReadFromKafka {

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "flink_consumer");

        DataStream<String> stream = env
                .addSource(new FlinkKafkaConsumer09<>("flink-demo", new SimpleStringSchema(), properties));

        env.enableCheckpointing(2000, CheckpointingMode.AT_LEAST_ONCE);

        RollingSink<String> sink = new RollingSink<String>("s3://flink-test/" + "TEST");
        sink.setBucketer(new DateTimeBucketer("yyyy-MM-dd--HHmm"));
        sink.setWriter(new StringWriter<String>());
        sink.setBatchSize(200);
        sink.setPendingPrefix("file-");
        sink.setPendingSuffix(".txt");
        stream.print();
        stream.addSink(sink).setParallelism(1);

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
