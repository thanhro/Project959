package com.thanhld.server959.service.assignments;

import com.google.api.services.drive.model.File;
import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.repository.AssignmentRepository;
import com.thanhld.server959.repository.ClassRepository;
import com.thanhld.server959.service.classes.ClassService;
import com.thanhld.server959.service.googledrive.GoogleDriveService;
import com.thanhld.server959.service.googledrive.GoogleDriveServiceUtils;
import com.thanhld.server959.utils.DateUtils;
import com.thanhld.server959.web.rest.errors.BadRequestAlertException;
import com.thanhld.server959.web.rest.errors.ErrorConstants;
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    GoogleDriveService googleDriveService;

    @Autowired
    GoogleDriveServiceUtils googleDriveServiceUtils;

    @Autowired
    ClassService classService;

    private String createFolderAssignment(String assignmentName, String webViewLink) {
        try {
            File folderParent = googleDriveService.getFolderByWebViewLink(webViewLink);
            // create assignment with folder parent over
            File fileMetaData = new File();
            fileMetaData.setName(assignmentName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            fileMetaData.setParents(Collections.singletonList(folderParent.getId()));
            File file = googleDriveServiceUtils.getService().files().create(fileMetaData).setFields("*").execute();
            googleDriveService.changeOwnerPermissionToCurrentUser(file);
            return file.getWebViewLink();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Assignment> findByClassCode(String classCode) {
        return assignmentRepository.findByClassCode(classCode);
    }

    @Override
    public String createAssignment(String classCode, Assignment assignment) {
        Class classObject = classRepository.findByCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not existed", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        String coach = SecurityUtils.getCurrentUserLogin().get().getId();
        if (classRepository.findByCodeAndCoach(classCode, coach) == null) {
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
    public void deleteAssignment(String assignmentName, String classCode) {
        boolean isTeacher = classService.isTeacher(classCode);
        Assignment assignment = validateAssignmentByNameAndClassCode(assignmentName, classCode, isTeacher);
        String assignmentLink = assignment.getLink();
        googleDriveService.deleteFileByLink(assignmentLink);
        assignmentRepository.deleteAssignmentByLink(assignmentLink);
    }

    @Override
    public void updateAssignment(Assignment assignment, String classCode) {
        boolean isTeacher = classService.isTeacher(classCode);
        Assignment assignmentObject = validateAssignmentByNameAndClassCode(assignment.getAssignmentName(), classCode, isTeacher);
        assignmentObject.setAssignmentName(assignment.getAssignmentName());
        assignmentObject.setAssignmentDescriptions(assignment.getAssignmentDescriptions());
        assignmentObject.setDueDate(assignment.getDueDate());
        assignmentRepository.save(assignmentObject);
        updateAssignmentFileName(assignment.getLink(), assignment.getAssignmentName());
    }

    @Override
    public Set<String> getAllUserSharedFileToTeacher(String assignmentName, String classCode) throws Exception {
        boolean isTeacher = classService.isTeacher(classCode);
        Assignment assignment = validateAssignmentByNameAndClassCode(assignmentName, classCode, isTeacher);
        if (isTeacher) {
            return googleDriveService.getAllDisplayNameInParentFile(assignment.getLink());
        }
        Set<String> userNames = googleDriveService.getAllDisplayNameInParentFile(assignment.getLink());

        String userName = userNames.stream().filter(SecurityUtils.getCurrentUserLogin().get().getUsername()::equals).findAny().orElse(null);
        Set<String> userSet = new HashSet<>();
        userSet.add(userName);
        return userSet;
    }


    @Override
    public Set<String> getAllUserDocsLinkSharedToTeacher(String assignmentName, String classCode) throws GeneralSecurityException, IOException {
        boolean isTeacher = classService.isTeacher(classCode);
        Assignment assignment = validateAssignmentByNameAndClassCode(assignmentName, classCode, isTeacher);
        return googleDriveService.getAllWebViewLinkInParentFile(assignment.getLink());
    }

    @Override
    public Map<String, String> getAllUserDocsAndLinkSharedToTeacher(String assignmentName, String classCode) throws GeneralSecurityException, IOException {
        boolean isTeacher = classService.isTeacher(classCode);
        Assignment assignment = validateAssignmentByNameAndClassCode(assignmentName, classCode, isTeacher);
        if (isTeacher) {
            return googleDriveService.getAllDisplayNameAndWebViewLinkInParentFile(assignment.getLink());
        }
        return googleDriveService.getDisplayNameAndWebViewLinkInParentFile(SecurityUtils.getCurrentUserLogin().get().getName(), assignment.getLink());
    }

    private void updateAssignmentFileName(String assignmentLink, String assignmentName) {
        googleDriveService.updateFileNameByLink(assignmentLink, assignmentName);
    }

    private Assignment validateAssignmentByNameAndClassCode(String assignmentName, String classCode, Boolean roleUser) {
        if (classRepository.findByCode(classCode) == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        Assignment assignment = assignmentRepository.findByNameAndClassCode(assignmentName, classCode);
        if (assignmentRepository.findByNameAndClassCode(assignmentName, classCode) == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Assignment not found", "Assignment", ErrorConstants.ASSIGNMENT_NOT_FOUND);
        }
        if (roleUser) {
            String teacherId = SecurityUtils.getCurrentUserLogin().get().getId();
            if (classRepository.findByCodeAndCoach(classCode, teacherId) == null) {
                throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_HAVE_PERMISSION, "User not have permission", "User permission", ErrorConstants.USER_NOT_HAVE_PERMISSION);
            }
        }
        return assignment;
    }

}
