package kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallback {

    public static void main(String[] args) {
       final Logger loggger = LoggerFactory.getLogger(ProducerWithCallback.class);

        //Create properties
        Properties properties = new Properties();
        String serverAddress = "127.0.0.1:9092";
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //Create producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for(int i=0; i < 10 ;i++) {

            ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "Hello World " + Integer.toString(i));
            //send data
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        loggger.info("Recevied Metadata Topic " + recordMetadata.topic());
                        loggger.info("Recevied Metadata Parttion " + recordMetadata.partition());
                        loggger.info("Recevied Metadata Offset " + recordMetadata.offset());
                        loggger.info("Recevied Metadata Timestamp " + recordMetadata.timestamp());


                    } else {
                        loggger.info("Error While producing " + e);

                    }
                }
            });
        }

        producer.flush();
        producer.close();
    }
}
