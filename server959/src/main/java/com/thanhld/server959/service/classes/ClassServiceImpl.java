package com.thanhld.server959.service.classes;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.utils.RandomCodeFactory;
import com.thanhld.server959.repository.ClassRepository;
import com.thanhld.server959.service.googledrive.GoogleDriveService;
import com.thanhld.server959.web.rest.errors.BadRequestAlertException;
import com.thanhld.server959.web.rest.errors.ErrorConstants;
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    GoogleDriveService googleDriveService;

    @Autowired
    ClassRepository classRepository;

    public List<Class> findAllClass() {
        return classRepository.findAll();
    }

    public void joinClassByCode(String classCode) {
        Class classObject = findByCode(classCode);
        if (classObject == null)
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found", "Class", ErrorConstants.CLASS_NOT_FOUND);
        String userId = SecurityUtils.getCurrentUserLogin().get().getId();
        List<String> listMemberId = classObject.getListMemeberId();
        if (!(listMemberId.contains(userId) || classObject.getCoach().equals(userId))) {
            listMemberId.add(userId);
            classObject.setListMemeberId(listMemberId);
            classRepository.save(classObject);
        }
    }

    @Override
    public void createClass(Class classContents) {
        String classCode = RandomCodeFactory.getRandomCode(4);
        String className = classContents.getClassName();
        String coach = SecurityUtils.getCurrentUserLogin().get().getId();
        String classDescription = classContents.getClassDescription();
        Class classObject = Class.builder()
                .classCode(classCode)
                .className(className)
                .classDescription(classDescription)
                .coach(coach)
                .listMemeberId(new ArrayList<>()).build();
        classRepository.save(classObject);
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
    public void updateClass(String classCode) {
        Class classObject = findByCode(classCode);
        if (classObject == null){
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        classRepository.save(classObject);
    }

    public Class findByCode(String classCode){
        Class classObject = classRepository.findByCode(classCode);
        if (classObject != null)
            return classObject;
        return null;
    }

    @Override
    public String createFolderClass(String className){
        return googleDriveService.createFolderClassAssignment(className);
    }
}
