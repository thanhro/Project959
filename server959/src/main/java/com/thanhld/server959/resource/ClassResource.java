package com.thanhld.server959.resource;

import com.thanhld.server959.model.Class;
import com.thanhld.server959.model.ResponseObjectFactory;
import com.thanhld.server959.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClassResource {

    @Autowired
    ClassService classService;

    @PostMapping("/course")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Class> getClassByCode(@RequestParam String code) {
        Class classroom = classService.getByCourseByCode(code);
        return ResponseObjectFactory.toResult(classroom, HttpStatus.OK);
    }

    @GetMapping("/courses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Class>> getAllClass() {
        List<Class> courses = classService.findAllCourse();
        if(courses == null || courses.isEmpty())
            return ResponseObjectFactory.toResult("Not have a class", HttpStatus.NO_CONTENT);
        return ResponseObjectFactory.toResult(courses, HttpStatus.NOT_FOUND);
    }
}
