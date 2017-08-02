package org.activiti.example.microservice.client;

import java.util.Map;


public class StartProcessCmd implements Command {

    private String processDefinitionKey;
    private Map<String, String> variables;

    public StartProcessCmd() {
    }

    public StartProcessCmd(String processDefinitionKey,
                           Map<String, String> variables) {
        this.processDefinitionKey = processDefinitionKey;
        this.variables = variables;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public Map<String, String> getVariables() {
        return variables;
    }


}
