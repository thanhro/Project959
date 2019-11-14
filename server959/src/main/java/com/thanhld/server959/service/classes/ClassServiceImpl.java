package com.thanhld.server959.service.classes;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.utils.RandomCodeFactory;
import com.thanhld.server959.repository.ClassRepository;
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
    ClassRepository classRepository;

    public List<Class> findAllClass() {
        return classRepository.findAll();
    }

    public Class joinClassByCode(String code) {
        Class classObject = classRepository.getClassByCode(code);
        if (classObject == null)
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found", "Class", ErrorConstants.CLASS_NOT_FOUND);
        String userId = SecurityUtils.getCurrentUserLogin().get().getId();
        List<String> listMemberId = classObject.getListMemeberId();
        if (!listMemberId.contains(userId)) {
            listMemberId.add(userId);
            classObject.setListMemeberId(listMemberId);
            updateClass(classObject);
        }
        return classRepository.getClassByCode(code);
    }

    @Override
    public void createClass(Class classContents) {
        String classCode = RandomCodeFactory.getRandomCode(4);
        String className = classContents.getClassName();
        String coach = classContents.getCoach();
        String classDescription = classContents.getClassDescription();
        Class classObject = new Class.ClassBuilder()
                .setClassCode(classCode)
                .setClassName(className)
                .setClassDescription(classDescription)
                .setClassCoach(coach)
                .setListMemeberId(new ArrayList<>()).build();
        classRepository.save(classObject);
    }

    @Override
    public void deleteClass(String classId) {
        if (!classRepository.findById(classId).isPresent())
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        classRepository.deleteById(classId);
    }

    @Override
    public void updateClass(Class classObject) {
        classRepository.save(classObject);
    }


}
