package stream;

import com.google.gson.JsonParser;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class StreamFiles {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG,"demo-kafka-stream");
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.StringSerde.class.getName());

        StreamsBuilder streamsBuilder = new StreamsBuilder();

       KStream<String,String> inputtopic =  streamsBuilder.stream("twitter_topic");
        KStream<String,String> filterStream = inputtopic.filter(
                (k,v) ->extractUserFollwerInTweet(v) > 10000
                );
        filterStream.to("import_tweets");

        KafkaStreams kafaStream =  new KafkaStreams(streamsBuilder.build(),
                properties);
    }

    private static JsonParser jsonParser = new JsonParser();
    private static int extractUserFollwerInTweet(String tweetJson) {
        try {
            return jsonParser.parse(tweetJson)
                    .getAsJsonObject()
                    .get("user")
                    .getAsJsonObject()
                    .get("follwer_count")
                    .getAsInt();
        }
        catch(Exception ex){
            return  0;
        }
    }
}
