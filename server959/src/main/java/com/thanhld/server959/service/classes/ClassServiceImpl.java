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

    public List<Class> findAllClass() {
        List<Class> listClasses = classRepository.findAll();
        List<Class> listClassesByUser = new ArrayList<>();
        String userId = SecurityUtils.getCurrentUserLogin().get().getId();
        for (Class classObject : listClasses) {
            if (userId.equals(classObject.getCoach()) || (classObject.getListMemeberId()).contains(userId)) {
                listClassesByUser.add(classObject);
            }
        }
        return listClassesByUser;
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
        Class classObject = validateClassByCode(classCode);
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
        Class classObject = validateClassByCode(classCode);
        classObject.setClassName(classContents.getClassName());
        classObject.setClassDescription(classContents.getClassDescription());
        classRepository.save(classObject);
        updateClassName(classContents.getGoogleDrive(), classContents.getClassName());
    }

    private void updateClassName(String googleDrive, String className) {
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
        List<User> listMembers = new ArrayList<>();
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        String coachClass =classObject.getCoach();
        Optional<User> userCoach = userRepository.findById(coachClass);
        listMembers.add(userCoach.get());

        List<String> listMemberId = classObject.getListMemeberId();
        if (listMemberId == null || listMemberId.isEmpty())
            return null;

        Optional<User> user;
        for (String id : listMemberId) {
            user = userRepository.findById(id);
            if (user.isPresent())
                listMembers.add(user.get());
        }
        return listMembers;
    }

    @Override
    public boolean isTeacher(String classCode) {
        String currentUserId = SecurityUtils.getCurrentUserLogin().get().getId();
        Class classObject = classRepository.findByCodeAndCoach(classCode, currentUserId);
        if (classObject != null) {
            return true;
        }
        return false;
    }

    @Override
    public Class findByClassCode(String classCode) {
        return classRepository.findByCode(classCode);
    }

    @Override
    public List<Class> findByUserId(String userId) {
        return classRepository.findByCoach(userId);
    }

    private Class validateClassByCode(String classCode) {
        Class classObject = findByCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        String teacherId = SecurityUtils.getCurrentUserLogin().get().getId();
        if (!isTeacher(classCode)) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_HAVE_PERMISSION, "User not have permission", "User permission", ErrorConstants.USER_NOT_HAVE_PERMISSION);
        }
        return classObject;
    }
}
