package com.thanhld.server959.service;

import com.thanhld.server959.model.Class;
import com.thanhld.server959.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    ClassRepository classRepository;

    public List<Class> findAllCourse() {
        List<Class> classes = classRepository.findAll();
    }

    public Class getByCourseByCode(String code) {
        return classRepository.getClassByCode(code);
    }
}
