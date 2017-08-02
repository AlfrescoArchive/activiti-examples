package org.activiti.example.microservice.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableBinding(CommandChannels.class)
@RestController
@RequestMapping(value = "/api/", produces = MediaTypes.HAL_JSON_VALUE)
public class MicroServiceExampleApplication {

    @Autowired
    private MessageChannel cmdOutput;

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceExampleApplication.class,
                              args);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/send")
    public ResponseEntity sendMessage() {
        Map<String, String> vars = new HashMap<>();
        vars.put("hey", "one");
        StartProcessCmd cmd = new StartProcessCmd("myId", vars);
        cmdOutput.send((MessageBuilder.withPayload(cmd).build()));
        return new ResponseEntity<String>(cmd + " ack!",
                                          HttpStatus.OK);
    }
}
