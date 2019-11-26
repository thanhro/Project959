package com.thanhld.server959.model.assignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thanhld.server959.model.AbstractModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "assignment")
public class Assignment extends AbstractModel {
    private String assignmentName;
    private String assignmentDescriptions;

    private String dueDate;
    private String link;
    private String classCode;
    private String createdAt;

    public Assignment() {
    }

    public Assignment(String assignmentName, String assignmentDescriptions, String dueDate, String link, String classCode, String createdAt) {
        this.assignmentName = assignmentName;
        this.assignmentDescriptions = assignmentDescriptions;
        this.dueDate = dueDate;
        this.link = link;
        this.classCode = classCode;
        this.createdAt = createdAt;
    }

    @JsonCreator
    Assignment(@JsonProperty("assignmentName") String assignmentName, @JsonProperty("assignmentDescriptions") String assignmentDescriptions, @JsonProperty("dueDate") String dueDate) {
        this.assignmentName = assignmentName;
        this.assignmentDescriptions = assignmentDescriptions;
        this.dueDate = dueDate;
    }
}
