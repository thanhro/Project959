package com.thanhld.server959.service.classes;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.user.User;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.util.List;

public interface ClassService {
    List<Class> findAllClass();

    void joinClassByCode(String code);

    Class createClass(Class classContents);

    void deleteClassByCode(String classCode) throws IOException;

    void updateClass(String classObject, Class classContents);

    String createFolderClass(String className);

    List<User> getAllClassMembers(String classCode);

    boolean isTeacher(String classCode);

    Class findByClassCode(String classCode);

    List<Class> findByUserId(String userId);
}
