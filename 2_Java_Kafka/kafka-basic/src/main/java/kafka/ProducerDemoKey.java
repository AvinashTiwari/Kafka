package kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoKey {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
       final Logger loggger = LoggerFactory.getLogger(ProducerDemoKey.class);

        //Create properties
        Properties properties = new Properties();
        String serverAddress = "127.0.0.1:9092";
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //Create producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for(int i=0; i < 10 ;i++) {
            String topic = "first_topic";
            String value = "Hello World "+  Integer.toString(i);
            String key = "Id_"+ Integer.toString(i);
            loggger.info("Key " + key);

            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);
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
            }).get();
        }

        producer.flush();
        producer.close();
    }
}
