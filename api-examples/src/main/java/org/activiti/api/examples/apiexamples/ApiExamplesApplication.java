package org.activiti.api.examples.apiexamples;

import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.TaskRuntime;
import org.activiti.runtime.api.conf.ProcessRuntimeConfiguration;
import org.activiti.runtime.api.connector.Connector;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.builders.ProcessPayloadBuilder;
import org.activiti.runtime.api.query.Page;
import org.activiti.runtime.api.query.Pageable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiExamplesApplication implements CommandLineRunner {

    private final ProcessRuntime processRuntime;

    private final TaskRuntime taskRuntime;

    public ApiExamplesApplication(ProcessRuntime processRuntime,
                                  TaskRuntime taskRuntime) {
        this.processRuntime = processRuntime;
        this.taskRuntime = taskRuntime;
    }

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

        ProcessInstance startedAndCompletedProcess = processRuntime.start(ProcessPayloadBuilder.start()
                                                                             .withProcessDefinitionKey("categorizeProcess")
                                                                             .withBusinessKey("my key").build());

        System.out.println("startedAndCompletedProcess - Process Instance: " + startedAndCompletedProcess);

        ProcessInstance startedProcess = processRuntime.start(ProcessPayloadBuilder.start()
                                                                 .withProcessDefinitionKey("categorizeHumanProcess")
                                                                 .withBusinessKey("my key").build());

        System.out.println("startedProcess - Process Instance: " + startedProcess);

        ProcessInstance startedProcess2 = processRuntime.start(ProcessPayloadBuilder.start()
                                                                  .withProcessDefinitionKey("categorizeHumanProcess")
                                                                  .withBusinessKey("my key").build());

        System.out.println("startedProcess2 - Process Instance: " + startedProcess2);

        ProcessInstance startedProcess3 = processRuntime.start(ProcessPayloadBuilder.start()
                                                                  .withProcessDefinitionKey("categorizeHumanProcess")
                                                                  .withBusinessKey("my key 2").build());

        System.out.println("startedProcess3 - Process Instance: " + startedProcess3);

        ProcessInstance suspendedProcess = processRuntime.suspend(ProcessPayloadBuilder.suspend(startedProcess));

        System.out.println("suspendedProcess - Process Instance: " + suspendedProcess);

        ProcessInstance resumedProcess = processRuntime.resume(ProcessPayloadBuilder.resume()
                                                                  .withProcessInstance(suspendedProcess)
                                                                  .build());

        System.out.println("resumedProcess - Process Instance: " + resumedProcess);

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

        ProcessInstance deletedProcess = processRuntime.delete(ProcessPayloadBuilder.delete()
                                                                  .withProcessInstance(suspendedProcess)
                                                                  .build());

        System.out.println("deletedProcess - Process Instance: " + deletedProcess);

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
