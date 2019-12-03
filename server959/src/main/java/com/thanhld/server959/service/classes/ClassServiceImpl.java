package com.thanhld.server959.service.classes;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.user.User;
import com.thanhld.server959.model.utils.RandomCodeFactory;
import com.thanhld.server959.repository.ClassRepository;
import com.thanhld.server959.repository.UserRepository;
import com.thanhld.server959.service.googledrive.GoogleDriveService;
import com.thanhld.server959.service.googledrive.GoogleDriveServiceUtils;
import com.thanhld.server959.web.rest.errors.BadRequestAlertException;
import com.thanhld.server959.web.rest.errors.ErrorConstants;
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    GoogleDriveService googleDriveService;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private Drive googleDrive;

    @Autowired
    private GoogleDriveServiceUtils googleDriveServiceUtils;

    public List<Class> findAllClass() {
        return classRepository.findAll();
    }

    public void joinClassByCode(String classCode) {
        Class classObject = findByCode(classCode);
        if (classObject == null)
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found", "Class", ErrorConstants.CLASS_NOT_FOUND);
        String userId = SecurityUtils.getCurrentUserLogin().get().getId();
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "User not found", "User", ErrorConstants.USER_NOT_FOUND);
        }
        List<String> listMemberId = classObject.getListMemeberId();
        if (!(listMemberId.contains(userId) || classObject.getCoach().equals(userId))) {
            listMemberId.add(userId);
            classObject.setListMemeberId(listMemberId);
            classRepository.save(classObject);
        }
    }

    @Override
    public Class createClass(Class classContents) {
        String classCode = RandomCodeFactory.getRandomCode(4);
        String className = classContents.getClassName();
        String coach = SecurityUtils.getCurrentUserLogin().get().getId();
        if (classRepository.findByNameAndCoach(classContents.getClassName(), coach) != null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_EXISTED, "Class existed", "Class", ErrorConstants.CLASS_ALREADY_EXISTED);
        }
        String classDescription = classContents.getClassDescription();
        Class classObject = Class.builder()
                .classCode(classCode)
                .className(className)
                .classDescription(classDescription)
                .coach(coach)
                .googleDrive(createFolderClass(className))
                .listMemeberId(new ArrayList<>()).build();
        classRepository.save(classObject);
        return classObject;
    }

    @Override
    public void deleteClassByCode(String classCode) throws IOException {
        Class classObject = findByCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        String teacherId = SecurityUtils.getCurrentUserLogin().get().getId();
        if (classRepository.findByCodeAndCoach(classCode, teacherId) == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_HAVE_PERMISSION, "User not have permission", "User permission", ErrorConstants.USER_NOT_HAVE_PERMISSION);
        }
        List<File> files = googleDriveService.getAllFiles();
        if (files == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_GOOGLE_DRIVE_NOT_FOUND, "File not found", "File", ErrorConstants.FILE_GOOGLE_DRIVE_NOT_FOUND);
        }
        for (File file : files) {
            if (file.getWebViewLink().equals(classObject.getGoogleDrive())) {
                googleDrive = googleDriveService.getService();
                if (googleDrive != null) {
                    googleDrive.files().delete(file.getId()).execute();
                }
            }
        }
        classRepository.delete(classObject);
    }

    @Override
    public void updateClass(String classCode, Class classContents) {
        if (classContents.getGoogleDrive() == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_PROPERTY_NOT_FOUND, "Class link not empty!", "Class Link", ErrorConstants.ASSIGNMENT_PROPERTY_NOT_FOUND);
        }
        Class classObject = findByCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        classObject.setClassName(classContents.getClassName());
        classObject.setClassDescription(classContents.getClassDescription());
        classRepository.save(classObject);
        upateClassName(classContents.getGoogleDrive(), classContents.getClassName());
    }

    private void upateClassName(String googleDrive, String className) {
        googleDriveService.updateFileNameByLink(googleDrive, className);
    }

    public Class findByCode(String classCode) {
        Class classObject = classRepository.findByCode(classCode);
        if (classObject != null)
            return classObject;
        return null;
    }

    @Override
    public String createFolderClass(String className) {
        try {
            return googleDriveService.createFolder(className);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllClassMembers(String classCode) {
        Class classObject = findByCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }

        List<String> listMemberId = classObject.getListMemeberId();
        if (listMemberId == null || listMemberId.isEmpty())
            return null;

        List<User> listMembers = new ArrayList<>();
        Optional<User> user;
        for (String id : listMemberId) {
            user = userRepository.findById(id);
            if (user.isPresent())
                listMembers.add(user.get());
        }
        return listMembers;
    }
}
