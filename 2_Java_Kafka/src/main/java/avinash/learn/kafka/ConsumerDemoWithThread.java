package avinash.learn.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThread {

    public ConsumerDemoWithThread(){}

    public void run(){
        final Logger loggger = LoggerFactory.getLogger(ConsumerDemoWithThread.class);

        Properties properties = new Properties();
        String serverAddress = "127.0.0.1:9092";
        String groupId = "my-sixth-application";
        String topic = "first_topic";
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        CountDownLatch latch = new CountDownLatch(1);
        Runnable myconsumerThread = new ConsumerThread(latch,
                topic,
                properties);

        Thread mythread = new Thread(myconsumerThread);
        mythread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            loggger.info("Caught Shutdown hook");
            ((ConsumerThread) myconsumerThread).shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
new ConsumerDemoWithThread().run();


    }

    public class ConsumerThread implements Runnable{
        private CountDownLatch latch;
        KafkaConsumer<String,String> consumer ;
        final Logger loggger = LoggerFactory.getLogger(ConsumerThread.class);

        public ConsumerThread(CountDownLatch latch, String topic,Properties properties ){
               this.latch = latch;

            consumer = new KafkaConsumer<String,String>(properties);

            consumer.subscribe(Arrays.asList(topic));

        }

        @Override
        public void run() {
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                    for (ConsumerRecord record : records) {
                        loggger.info("key :" + record.key() + " Value: " + record.value());
                        loggger.info("partion :" + record.partition() + " offset: " + record.offset());

                    }
                }
            }catch(WakeupException ex){
                loggger.info("Shutdown Signal");
            }finally {
                consumer.close();
                latch.countDown();
            }

        }

        public void shutdown(){
            consumer.wakeup();
        }
    }
}
