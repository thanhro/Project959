package com.thanhld.server959.model.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.thanhld.server959.model.AbstractModel;
import com.thanhld.server959.utils.DateHandler;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Assignment extends AbstractModel {
    private String assignmentName;
    private String assignmentDescriptions;

    @JsonDeserialize(using = DateHandler.class)
    private String dueDate;
    private String link;
    private String createdAt;

    public Assignment() {
    }

    public Assignment(String assignmentName, String assignmentDescriptions, String dueDate, String link, String createdAt) {
        this.assignmentName = assignmentName;
        this.assignmentDescriptions = assignmentDescriptions;
        this.dueDate = dueDate;
        this.link = link;
        this.createdAt = createdAt;
    }

    Assignment(@JsonProperty("assignmentName") String assignmentName, @JsonProperty("assignmentDescriptions") String assignmentDescriptions, @JsonProperty("dueDate") String dueDate) {
        this.assignmentName = assignmentName;
        this.assignmentDescriptions = assignmentDescriptions;
        this.dueDate = dueDate;
    }
}
