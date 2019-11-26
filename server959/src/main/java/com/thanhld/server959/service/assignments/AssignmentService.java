package com.thanhld.server959.service.assignments;

import com.thanhld.server959.model.assignment.Assignment;

import java.util.Set;

public interface AssignmentService {
    String createAssignment(String classCode, Assignment assignment);
    Set<String> getAllUserSharedFileToTeacher(String classCode) throws Exception;
}
