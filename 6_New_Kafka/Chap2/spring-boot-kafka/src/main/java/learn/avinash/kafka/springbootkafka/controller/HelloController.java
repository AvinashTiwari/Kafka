package learn.avinash.kafka.springbootkafka.controller;

import learn.avinash.kafka.springbootkafka.producer.ProducerKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HelloController {

    @Autowired
    Environment env;

    @Autowired
    ProducerKafka producer;

    @RequestMapping(value="/home")
    public String getResult(@RequestParam("input") String value){

        producer.sendMessage(value);
        return env.getProperty("message.response");
    }

}