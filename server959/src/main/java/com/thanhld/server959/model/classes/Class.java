package com.thanhld.server959.model.classes;

import com.thanhld.server959.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Class extends AbstractModel {
    private String id;
    private String className;
    private String classDescription;
    private String classCode;
    private String coach;

    public Class(ClassBuilder classBuilder) {
        this.id = classBuilder.id;
        this.className = classBuilder.className;
        this.classDescription = classBuilder.classDescription;
        this.classCode = classBuilder.classCode;
        this.coach = classBuilder.coach;
    }

    public static class ClassBuilder {
        private String id;
        private String className;
        private String classDescription;
        private String classCode;
        private String coach;

        public ClassBuilder() {

        }

        public ClassBuilder(String className, String classCode, String coach) {
            this.className = className;
            this.classCode = classCode;
            this.coach = coach;
        }

        public ClassBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public ClassBuilder setClassName(String className) {
            this.className = className;
            return this;
        }

        public ClassBuilder setClassDescription(String classDescription) {
            this.classDescription = classDescription;
            return this;
        }

        public ClassBuilder setClassCode(String classCode) {
            this.classCode = classCode;
            return this;
        }

        public ClassBuilder setClassCoach(String coach) {
            this.coach = coach;
            return this;
        }

        public Class build() {
            return new Class(this);
        }
    }
}
