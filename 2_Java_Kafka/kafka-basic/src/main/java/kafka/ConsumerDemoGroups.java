package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoGroups {

    public static void main(String[] args) {
        final Logger loggger = LoggerFactory.getLogger(ConsumerDemoGroups.class);

        Properties properties = new Properties();
        String serverAddress = "127.0.0.1:9092";
        String groupId = "my-fifth-application";
        String topic = "first_topic";
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);

        consumer.subscribe(Arrays.asList(topic));

        while(true){
         ConsumerRecords<String,String> records =  consumer.poll(Duration.ofMillis(100));

         for(ConsumerRecord record: records){
             loggger.info("key :" + record.key() + " Value: "+ record.value());
             loggger.info("partion :" + record.partition() + " offset: "+ record.offset());

         }
        }


    }
}
