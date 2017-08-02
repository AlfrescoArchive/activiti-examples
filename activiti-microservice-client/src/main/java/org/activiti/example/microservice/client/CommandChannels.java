package org.activiti.example.microservice.client;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CommandChannels {

    String CMD_OUTPUT = "cmdOutput";

    String CMD_RESULTS = "cmdResults";

    @Output(CMD_OUTPUT)
    MessageChannel cmdOutput();

    @Input(CMD_RESULTS)
    SubscribableChannel cmdResults();
}
