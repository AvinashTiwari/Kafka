package twitter.producer.comsumer;

import avinash.learn.kafka.ConsumerDemoAssignSeek;
import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {
    final Logger loggger = LoggerFactory.getLogger(TwitterProducer.class);
    String consumerKey ="hrkH4acVqNGGYRL600pjpzX07";
    String consumerSecret ="rXCin19lDDnOGTc7r9XFfpziUNvzjRXyba2mQYhoeel0UAV9dr";
    String token ="89983730-XGiker0pIlvbQ7JpFonaBTXuIYsNa9wQsaBfqp3Pb";
    String secret ="WOuMDyfqIkYQc8dnbguRMXZkloNvJFcbfdag3x4AgZspF";
    public TwitterProducer(){

    }
    public static void main(String[] args) {
      new TwitterProducer().run();
    }

    public void run(){
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);

        Client client = createTwitterClinet(msgQueue);
        client.connect();

        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            if(msg !=null){
                loggger.info(msg);
            }


        }
        loggger.info("Applciation End");

    }


    public Client createTwitterClinet(BlockingQueue<String>  msgQueue){

        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms
        List<String> terms = Lists.newArrayList("Kafka");
        hosebirdEndpoint.trackTerms(terms);

// These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);



        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        Client hosebirdClient = builder.build();
        return hosebirdClient;

// Attempts to establish a connection.


    }
}
