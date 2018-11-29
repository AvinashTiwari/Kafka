package consumer.elastic;

import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ElasticSerachConsumer {


    public static RestHighLevelClient createClient(){

        String hostName = "kafka-course-6776825292.us-east-1.bonsaisearch.net";
        String userNAme ="91173n89ur";
        String password ="zspsx06t3o";
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                 new UsernamePasswordCredentials(userNAme, password));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostName, 443,"https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                        return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });


       RestHighLevelClient client = new RestHighLevelClient(builder);

       return  client;
    }

    public static void main(String[] args) throws IOException {
        final Logger loggger = LoggerFactory.getLogger(ElasticSerachConsumer.class.getName());

        RestHighLevelClient client = createClient();
               KafkaConsumer<String,String> consumer= createConsumer("twitter_tweets");



        while(true){
            ConsumerRecords<String,String> records =  consumer.poll(Duration.ofMillis(100));
            Integer recordCount = records.count();
            loggger.info("Recived " + recordCount + " records" );

            BulkRequest bulkRequest = new BulkRequest();

            for(ConsumerRecord<String,String>  record: records){

               String id = "";
                try {
                     id = extractIdFromTweet(record.value());
                }catch(Exception ex){
                    id = record.topic()  + "_" + record.topic() + "_" + record.offset();
                }


                 IndexRequest indexRequest = new IndexRequest("twitter","tweets",
                         id)
                        .source(record.value(), XContentType.JSON);

                bulkRequest.add(indexRequest);
              //  IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                //String id = indexResponse.getId();
               // loggger.info(indexResponse.getId());

              //  try {
                //    Thread.sleep(10);
                //} catch (InterruptedException e) {
                   // e.printStackTrace();
                //}


            }

            if(recordCount > 0) {

                BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

                loggger.info("Commiting offset..");
                consumer.commitAsync();
                loggger.info("Offset had been  cmmited..");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }


      //  client.close();
    }

   private static JsonParser jsonParser = new JsonParser();
    private static String extractIdFromTweet(String tweetJson) {
        return jsonParser.parse(tweetJson)
                .getAsJsonObject()
                .get("id_str")
                .getAsString();
    }

    public static KafkaConsumer<String, String > createConsumer(String topic){
        final Logger loggger = LoggerFactory.getLogger(ElasticSerachConsumer.class);

        Properties properties = new Properties();
        String serverAddress = "127.0.0.1:9092";
        String groupId = "Kafka-demo-elastic-search";

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"100");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);
        consumer.subscribe(Arrays.asList(topic));
        return consumer;


    }
}
