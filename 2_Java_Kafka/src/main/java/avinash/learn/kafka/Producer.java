package avinash.learn.kafka;

import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

    public static void main(String[] args) {
       //Create properties
        Properties properties = new Properties();
        String serverAddress = "127.0.0.1:9092";
        properties.setProperty("bootstrap.servers", serverAddress);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());


        //Create producer

        //send data
    }
}
