package com.thanhld.server959.model.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thanhld.server959.model.AbstractModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Builder
@Document(collection = "class")
public class Class extends AbstractModel {
    private String className;
    private String classDescription;
    private String classCode;
    private String coach;
    private String googleDrive;
    private List<String> listMemeberId;

    public Class() {
    }

    public Class(String className, String classDescription, String classCode, String coach, String googleDrive, List<String> listMemeberId) {
        this.className = className;
        this.classDescription = classDescription;
        this.classCode = classCode;
        this.coach = coach;
        this.googleDrive = googleDrive;
        this.listMemeberId = listMemeberId;
    }

    @JsonCreator
    Class(@JsonProperty("className") String className, @JsonProperty("classDescription") String classDescription) {
        this.className = className;
        this.classDescription = classDescription;
    }

}
