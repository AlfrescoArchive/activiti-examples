package org.activiti.api.examples.apiexamples;

import java.util.HashMap;

import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.conf.ProcessRuntimeConfiguration;
import org.activiti.runtime.api.connector.Connector;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.builder.PayloadBuilder;
import org.activiti.runtime.api.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiExamplesApplication implements CommandLineRunner {

    @Autowired
    private ProcessRuntime processRuntime;

    public static void main(String[] args) {
        SpringApplication.run(ApiExamplesApplication.class,
                              args);
    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        ProcessRuntimeConfiguration configuration = processRuntime.configuration();
//
//
//        // @TODO: configuration should also have the directory from where the processes are being picked up
//        // @TODO: configuration can also have the default page size for the service calls
//        System.out.println("Configuration: " + configuration);
//
//        Page<FluentProcessDefinition> fluentProcessDefinitionPage =
//                processRuntime.processDefinitions();
//
//        for (FluentProcessDefinition pd : fluentProcessDefinitionPage.getContent()) {
//            System.out.println("Process Definition: " + pd);
//        }
//
//        FluentProcessInstance fluentProcessInstance = processRuntime
//                .processDefinitionByKey("categorizeProcess")
//                .startProcessWith()
//                .variable("var",
//                          "value")
//                .doIt();
//
//        System.out.println("Process Instance: " + fluentProcessInstance);
//    }

    public void run(String... args) throws Exception {
        ProcessRuntimeConfiguration configuration = processRuntime.configuration();

        Page<ProcessDefinition> processDefinitionPage =
                processRuntime.processDefinitions();

        for (ProcessDefinition pd : processDefinitionPage.getContent()) {
            System.out.println("Process Definition: " + pd);
        }

        ProcessInstance startedAndCompletedPi = processRuntime.start(PayloadBuilder.start()
                                                                 .withProcessDefinitionKey("categorizeProcess")
                                                                 .withVariables(new HashMap<>())
                                                                 .withBusinessKey("my key").build());

        System.out.println("startedAndCompletedPi - Process Instance: " + startedAndCompletedPi);

        ProcessInstance startedPi = processRuntime.start(PayloadBuilder.start()
                                                                 .withProcessDefinitionKey("categorizeHumanProcess")
                                                                 .withVariables(new HashMap<>())
                                                                 .withBusinessKey("my key").build());

        System.out.println("startedPi - Process Instance: " + startedPi);

        ProcessInstance suspendedPi = processRuntime.suspend(PayloadBuilder.suspend()
                                                                     .withProcessInstance(startedPi)
                                                                     .build());

        System.out.println("suspendedPi - Process Instance: " + suspendedPi);

        ProcessInstance resumedPi = processRuntime.resume(PayloadBuilder.resume()
                                                                  .withProcessInstance(suspendedPi)
                                                                  .build());

        System.out.println("resumedPi - Process Instance: " + resumedPi);

        ProcessInstance deletedPi = processRuntime.delete(PayloadBuilder.delete()
                                                                  .withProcessInstance(suspendedPi)
                                                                  .build());

        System.out.println("deletedPi - Process Instance: " + deletedPi);




    }

    @Bean
    public Connector processImageConnector() {
        return integrationContext -> {
            System.out.println(">>> Process Image Connector");
            integrationContext.addOutBoundVariable("approved",
                                                   "false");
            return integrationContext;
        };
    }

    @Bean
    public Connector tagImageConnector() {
        return integrationContext -> {
            System.out.println(">>> Tag Image Connector");
            return integrationContext;
        };
    }

    @Bean
    public Connector discardImageConnector() {
        return integrationContext -> {
            System.out.println(">>> Discard Image Connector");
            return integrationContext;
        };
    }
}
