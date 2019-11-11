package com.thanhld.server959.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Class extends AbstractModel {
    private String id;
    private String className;
    private String classDescription;
    private String classCode;
    private String coach;
}
