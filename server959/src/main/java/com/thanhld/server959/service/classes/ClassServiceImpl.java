package com.thanhld.server959.service.classes;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.utils.RandomCodeFactory;
import com.thanhld.server959.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService{
    @Autowired
    ClassRepository classRepository;

    public List<Class> findAllClass() {
        return classRepository.findAll();
    }

    public Class getByClassByCode(String code) {
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
                                                    .setClassCoach(coach).build();
        classRepository.save(classObject);
    }

    //Fixme
    @Override
    public void deleteClass(String classId) {

    }
}
