package com.thanhld.server959.service.assignments;

import com.google.api.services.drive.model.File;
import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.repository.AssignmentRepository;
import com.thanhld.server959.repository.ClassRepository;
import com.thanhld.server959.service.googledrive.GoogleDriveService;
import com.thanhld.server959.utils.DateUtils;
import com.thanhld.server959.utils.GoogleDriveUtils;
import com.thanhld.server959.web.rest.errors.BadRequestAlertException;
import com.thanhld.server959.web.rest.errors.ErrorConstants;
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    GoogleDriveService googleDriveService;

    private String createFolderAssignment(String assignmentName, String webViewLink) {
        try {
            File folderParent = googleDriveService.getFolderByWebViewLink(webViewLink);
            // create assignment with folder parent over
            File fileMetaData = new File();
            fileMetaData.setName(assignmentName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            fileMetaData.setParents(Collections.singletonList(folderParent.getId()));
            return GoogleDriveUtils.getService().files().create(fileMetaData).setFields("*").execute().getWebViewLink();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String createAssignment(String classCode, Assignment assignment) {
        Class classObject = classRepository.findByCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not existed", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        String coach = SecurityUtils.getCurrentUserLogin().get().getId();
        if (classRepository.findByNameAndCoach(classCode, coach) == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_EXISTED, "User not have permission", "User", ErrorConstants.USER_NOT_HAVE_PERMISSION);
        }
        if (assignmentRepository.findByNameAndClassCode(assignment.getAssignmentName(), classCode) != null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_EXISTED, "Assignment existed", "Assignment", ErrorConstants.ASSIGNMENT_ALREADY_EXISTED);
        }
        Assignment assignmentObject = Assignment.builder()
                .assignmentName(assignment.getAssignmentName())
                .assignmentDescriptions(assignment.getAssignmentDescriptions())
                .dueDate(assignment.getDueDate())
                .classCode(classCode)
                .link(createFolderAssignment(assignment.getAssignmentName(), classObject.getGoogleDrive()))
                .createdAt(DateUtils.simpleDateFormat(new Date())).build();
        assignmentRepository.save(assignmentObject);
        return assignmentObject.getLink();
    }

    @Override
    public Set<String> getAllUserSharedFileToTeacher(String classCode) throws Exception {
        if (assignmentRepository.findByCode(classCode) == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Assignment not existed", "Assignment", ErrorConstants.CLASS_NOT_FOUND);
        }
        return googleDriveService.getAllOwnersSharedFileToTeacher();
    }
}
