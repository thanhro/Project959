package com.thanhld.server959.resource;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.security.ResponseObjectFactory;
import com.thanhld.server959.service.classes.ClassService;
import com.thanhld.server959.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassResource {

    @Autowired
    ClassService classService;

    @Autowired
    UserService userService;

    @GetMapping("/join/class")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Class> getClassByCode(@RequestParam String code) {
        Class classroom = classService.joinClassByCode(code);
        return ResponseObjectFactory.toResult(classroom, HttpStatus.OK);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Class>> getAllClass() {
        List<Class> courses = classService.findAllClass();
        return ResponseObjectFactory.toResult(courses, HttpStatus.OK);
    }

    @PostMapping("/class")
    public ResponseEntity<Void> createClass(@RequestBody Class classContents) {
        classService.createClass(classContents);
        return ResponseObjectFactory.toResult("Successfully", HttpStatus.OK);
    }
}
