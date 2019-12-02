package com.thanhld.server959.service.classes;

import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.thanhld.server959.config.GoogleDriveServiceConfig;
import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.user.User;
import com.thanhld.server959.model.utils.RandomCodeFactory;
import com.thanhld.server959.repository.ClassRepository;
import com.thanhld.server959.repository.UserRepository;
import com.thanhld.server959.service.googledrive.GoogleDriveService;
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
    private GoogleDriveServiceConfig googleDriveServiceConfig;

    public List<Class> findAllClass() {
        try {
            Drive drive = googleDriveServiceConfig.getService();

            JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
                @Override
                public void onSuccess(Permission permission, HttpHeaders httpHeaders) throws IOException {
                    System.out.println("Permission ID: " + permission.getId());
                }

                @Override
                public void onFailure(GoogleJsonError googleJsonError, HttpHeaders httpHeaders) throws IOException {
                    System.err.println(googleJsonError.getMessage());
                }
            };
            try {
                FileList fileList = drive.files().list()
                        .setFields("nextPageToken, files(*)")
                        .execute();
                List<File> files = fileList.getFiles();
                for (File file : files) {
                    System.out.println(file.getId());
                    System.out.println(file.getPermissions());
                }

                drive.permissions().delete("1pvvrsWAhV7_XKe8-lHT5oatnFQZucgUrzWXnKKbn2Dg", "11652920652362738064").execute();

//                File fileMetadata = new File();
//                fileMetadata.setName("My Report");
//                fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
//
//                File file = drive.files().create(fileMetadata)
//                        .setFields("*")
//                        .execute();
//                System.out.println("File ID: " + file.getId());
//
//
//                BatchRequest batch = drive.batch();
//                Permission userPermission = new Permission().setType("user").setRole("owner").setEmailAddress("langquet@gmail.com");
//                drive.permissions().create(file.getId(),userPermission).setTransferOwnership(true).setFields("*").queue(batch,callback);
//                batch.execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classRepository.findAll();

    }

    public void joinClassByCode(String classCode) {
        Class classObject = findByCode(classCode);
        if (classObject == null)
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found", "Class", ErrorConstants.CLASS_NOT_FOUND);
        String userId = SecurityUtils.getCurrentUserLogin().get().getId();
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
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
    public void deleteClassByCode(String classCode) {
        Class classObject = findByCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        classRepository.delete(classObject);
    }

    @Override
    public void updateClass(String classCode, Class classContents) {
        Class classObject = findByCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        classObject.setClassName(classContents.getClassName());
        classObject.setClassDescription(classContents.getClassDescription());
        classRepository.save(classObject);
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

        List<User> listMembers = null;
        Optional<User> user;
        for (String id : listMemberId) {
            user = userRepository.findById(id);
            if (user.isPresent())
                listMembers.add(user.get());
        }
        return listMembers;
    }
}
