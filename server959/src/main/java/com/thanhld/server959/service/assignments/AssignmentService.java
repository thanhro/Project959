package com.thanhld.server959.service.assignments;

import com.thanhld.server959.model.assignment.Assignment;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

public interface AssignmentService {
    String createAssignment(String classCode, Assignment assignment);
    Set<String> getAllUserSharedFileToTeacher(String assignmentLink) throws Exception;
    void deleteAssignment(String assignmentLink);
    void updateAssignment(Assignment assignment);
    Set<String> getAllUserDocsLinkSharedToTeacher(String assignmentLink) throws GeneralSecurityException, IOException;
}
