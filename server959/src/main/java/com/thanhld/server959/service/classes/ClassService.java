package com.thanhld.server959.service.classes;

import com.thanhld.server959.model.classes.Class;

import java.util.List;

public interface ClassService {
    List<Class> findAllClass();

    void joinClassByCode(String code);

    void createClass(Class classContents);

    void deleteClassByCode(String classId);

    void updateClass(String classObject);

    String createFolderClass(String className);
}
