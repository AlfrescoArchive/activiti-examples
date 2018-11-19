package org.activiti.examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,property = "@class")
public class Content {

    private String body;
    private boolean approved;

    @JsonCreator
    public Content(@JsonProperty("body")String body, @JsonProperty("approved")boolean approved){
        this.body = body;
        this.approved = approved;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Content{" +
                "body='" + body + '\'' +
                ", approved=" + approved +
                '}';
    }
}
