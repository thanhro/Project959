package com.thanhld.server959.resource;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.security.ResponseObjectFactory;
import com.thanhld.server959.service.classes.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClassResource {

    @Autowired
    ClassService classService;

    @PostMapping("/class")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Class> getClassByCode(@RequestParam String code) {
        Class classroom = classService.getByClassByCode(code);
        return ResponseObjectFactory.toResult(classroom, HttpStatus.OK);
    }

    @GetMapping("/classes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Class>> getAllClass() {
        List<Class> courses = classService.findAllClass();
        if (courses == null || courses.isEmpty())
            return ResponseObjectFactory.toResult("Not have a classes", HttpStatus.NO_CONTENT);
        return ResponseObjectFactory.toResult(courses, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/class")
    public ResponseEntity<Void> createClass(@RequestBody Class classContents){
        classService.createClass(classContents);
        return ResponseObjectFactory.toResult("Error", HttpStatus.BAD_REQUEST);
    }
}
