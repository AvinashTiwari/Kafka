package learn.avinash.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

public class wordcount {
    public static void main(String[] args) {

        Properties config = new Properties();
        config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG,"wordcount-application");
        config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        KStreamBuilder builder = new KStreamBuilder();
         KStream<String,String> wordCountInput =  builder.stream("word-count-input");

      KTable<String, Long> wordCounts = wordCountInput.mapValues(value -> value.toLowerCase())
        .flatMapValues(lowercasetextline-> Arrays.asList(lowercasetextline.split(" ")))
        .selectKey((ignoredkey,word)->word)
        .groupByKey()
        .count("Counts");

        wordCounts.to(Serdes.String(), Serdes.Long(),"word-count-input");

        KafkaStreams streams = new KafkaStreams(builder,config);
        streams.start();
        System.out.println(streams.toString());



    }
}
