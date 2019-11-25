package com.thanhld.server959.service.assignments;

import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.repository.AssignmentRepository;
import com.thanhld.server959.service.googledrive.GoogleDriveService;
import com.thanhld.server959.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    GoogleDriveService googleDriveService;

    private String createFolderAssignment(String className) {
        return googleDriveService.createFolderClassAssignment(className);
    }

    @Override
    public String createAssignment(Assignment assignment) {
        Assignment assignmentObject = Assignment.builder()
                .assignmentName(assignment.getAssignmentName())
                .assignmentDescriptions(assignment.getAssignmentDescriptions())
                .dueDate(assignment.getDueDate())
                .link(createFolderAssignment(assignment.getAssignmentName()))
                .createdAt(DateUtils.simpleDateFormat(new Date())).build();
        assignmentRepository.save(assignmentObject);
        return createFolderAssignment(assignment.getAssignmentName());
    }
}
