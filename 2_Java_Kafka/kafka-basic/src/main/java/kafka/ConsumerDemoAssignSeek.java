package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoAssignSeek {

    public static void main(String[] args) {
        final Logger loggger = LoggerFactory.getLogger(ConsumerDemoAssignSeek.class);
        String topic = "first_topic";

        Properties properties = new Properties();
        String serverAddress = "127.0.0.1:9092";
          properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
         properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);

        TopicPartition topicPartionReadForm =new TopicPartition(topic, 5);
        consumer.assign(Arrays.asList(topicPartionReadForm));

        long OffsetReadfrom =15L;
        consumer.seek(topicPartionReadForm,OffsetReadfrom);

        int numberOfMessaegToRead = 5;
        boolean keepReading = true;
        int numberOfMessageSofaread = 0;
        while(true){
         ConsumerRecords<String,String> records =  consumer.poll(Duration.ofMillis(100));

         for(ConsumerRecord record: records){
             loggger.info("key :" + record.key() + " Value: "+ record.value());
             loggger.info("partion :" + record.partition() + " offset: "+ record.offset());

             if(numberOfMessageSofaread >= numberOfMessaegToRead){
                 keepReading = false;
                 break;
             }

         }
        }


    }
}
