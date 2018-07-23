package org.activiti.api.examples.apiexamples;

import java.util.HashMap;

import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.TaskRuntime;
import org.activiti.runtime.api.conf.ProcessRuntimeConfiguration;
import org.activiti.runtime.api.connector.Connector;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.builder.ProcessPayloadBuilder;
import org.activiti.runtime.api.query.Page;
import org.activiti.runtime.api.query.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiExamplesApplication implements CommandLineRunner {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    public static void main(String[] args) {
        SpringApplication.run(ApiExamplesApplication.class,
                              args);
    }

    public void run(String... args) throws Exception {
        ProcessRuntimeConfiguration configuration = processRuntime.configuration();

        Page<ProcessDefinition> processDefinitionPage =
                processRuntime.processDefinitions(Pageable.of(0,
                                                              50));

        for (ProcessDefinition pd : processDefinitionPage.getContent()) {
            System.out.println("Process Definition: " + pd);
        }

        ProcessInstance startedAndCompletedPi = processRuntime.start(ProcessPayloadBuilder.start()
                                                                             .withProcessDefinitionKey("categorizeProcess")
                                                                             .withVariables(new HashMap<>())
                                                                             .withBusinessKey("my key").build());

        System.out.println("startedAndCompletedPi - Process Instance: " + startedAndCompletedPi);

        ProcessInstance startedPi = processRuntime.start(ProcessPayloadBuilder.start()
                                                                 .withProcessDefinitionKey("categorizeHumanProcess")
                                                                 .withVariables(new HashMap<>())
                                                                 .withBusinessKey("my key").build());

        System.out.println("startedPi - Process Instance: " + startedPi);

        ProcessInstance startedPi2 = processRuntime.start(ProcessPayloadBuilder.start()
                                                                  .withProcessDefinitionKey("categorizeHumanProcess")
                                                                  .withVariables(new HashMap<>())
                                                                  .withBusinessKey("my key").build());

        System.out.println("startedPi2 - Process Instance: " + startedPi);

        ProcessInstance startedPi3 = processRuntime.start(ProcessPayloadBuilder.start()
                                                                  .withProcessDefinitionKey("categorizeHumanProcess")
                                                                  .withVariables(new HashMap<>())
                                                                  .withBusinessKey("my key 2").build());

        System.out.println("startedPi3 - Process Instance: " + startedPi);

        ProcessInstance suspendedPi = processRuntime.suspend(ProcessPayloadBuilder.suspend(startedPi));

        System.out.println("suspendedPi - Process Instance: " + suspendedPi);

        ProcessInstance resumedPi = processRuntime.resume(ProcessPayloadBuilder.resume()
                                                                  .withProcessInstance(suspendedPi)
                                                                  .build());

        System.out.println("resumedPi - Process Instance: " + resumedPi);

        Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(0,
                                                                                                50));

        System.out.println("Process Instances Count (1): " + processInstancePage.getTotalItems());

        processInstancePage = processRuntime.processInstances(Pageable.of(0,
                                                                          50),
                                                              ProcessPayloadBuilder
                                                                      .processInstances()
                                                                      .withBusinessKey("my key")
                                                                      .build());

        System.out.println("Process Instances Count (2): " + processInstancePage.getTotalItems());

        processInstancePage = processRuntime.processInstances(Pageable.of(0,
                                                                          50),
                                                              ProcessPayloadBuilder
                                                                      .processInstances()
                                                                      .withBusinessKey("my key 2")
                                                                      .build());

        System.out.println("Process Instances Count (3): " + processInstancePage.getTotalItems());



        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0,
                                                         50));

        System.out.println("Tasks Count: " + tasks.getTotalItems());

        for (Task t : tasks.getContent()) {
            System.out.println("Task: " + t);
        }

        ProcessInstance deletedPi = processRuntime.delete(ProcessPayloadBuilder.delete()
                                                                  .withProcessInstance(suspendedPi)
                                                                  .build());

        System.out.println("deletedPi - Process Instance: " + deletedPi);

        processInstancePage = processRuntime.processInstances(Pageable.of(0,
                                                                          50));

        System.out.println("Process Instances Count (4): " + processInstancePage.getTotalItems());
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
