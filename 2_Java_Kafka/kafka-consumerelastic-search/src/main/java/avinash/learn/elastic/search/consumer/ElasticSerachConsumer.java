package avinash.learn.elastic.search.consumer;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
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
        String jsonString ="{\"foo\": \"bar\"}";
        IndexRequest indexRequest = new IndexRequest("twitter","tweets")
                .source(jsonString, XContentType.JSON);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        String id = indexResponse.getId();
        loggger.info(id);

        client.close();
    }
}
