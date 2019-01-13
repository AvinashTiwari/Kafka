package learn.avinash.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class Starterapp {
    public static void main(String[] args) {

        Properties config = new Properties();
        config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG,"stream-starter-app");
        config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());



    }
}
