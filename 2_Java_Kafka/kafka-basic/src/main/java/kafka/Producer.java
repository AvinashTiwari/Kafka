package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

    public static void main(String[] args) {
       //Create properties
        Properties properties = new Properties();
        String serverAddress = "127.0.0.1:9092";
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //Create producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String,String> record = new ProducerRecord<String,String>("first_topic", "Hello World");
        //send data
        producer.send(record);

        producer.flush();
        producer.close();
    }
}
