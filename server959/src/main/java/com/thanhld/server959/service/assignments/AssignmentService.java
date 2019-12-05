package com.thanhld.server959.service.assignments;

import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.classes.Class;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AssignmentService {
    List<Assignment> findByClassCode(List<Class> classes);

    List<Assignment> findByClassCode(String classCode);

    String createAssignment(String classCode, Assignment assignment);

    void deleteAssignment(String assignmentName, String classCode);

    void updateAssignment(Assignment assignment, String classCode);

    Set<String> getAllUserSharedFileToTeacher(String assignmentName, String classCode) throws Exception;

    Set<String> getAllUserDocsLinkSharedToTeacher(String assignmentName, String classCode) throws GeneralSecurityException, IOException;

    Map<String, String> getAllUserDocsAndLinkSharedToTeacher(String assignmentName, String classCode) throws GeneralSecurityException, IOException;

    List<Assignment> getAllAssignment(String classCode);
}
