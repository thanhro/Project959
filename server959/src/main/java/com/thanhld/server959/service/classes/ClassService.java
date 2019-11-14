package com.thanhld.server959.service.classes;

import com.thanhld.server959.model.classes.Class;

import java.util.List;

public interface ClassService {
    List<Class> findAllClass();

    Class joinClassByCode(String code);

    void createClass(Class classContents);

    void deleteClass(String classId);

    void updateClass(Class classObject);
}
