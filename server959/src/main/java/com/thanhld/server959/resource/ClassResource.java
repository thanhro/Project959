package com.thanhld.server959.resource;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.security.ResponseObjectFactory;
import com.thanhld.server959.model.user.User;
import com.thanhld.server959.service.classes.ClassService;
import com.thanhld.server959.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        classService.joinClassByCode(code);
        return ResponseObjectFactory.toResult("Successfully", HttpStatus.OK);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Class>> getAllClass() {
        List<Class> courses = classService.findAllClass();
        return ResponseObjectFactory.toResult(courses, HttpStatus.OK);
    }

    //create class
    @PostMapping(value = "/class", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createClass(@RequestBody Class classesContents) {
        Class classObject = classService.createClass(classesContents);
        return ResponseObjectFactory.toResult(classObject.getGoogleDrive(), HttpStatus.OK);
    }

    @DeleteMapping("/class")
    public ResponseEntity<Void> deleteClassByCode(@RequestParam("classCode") String classCode) {
        try {
            classService.deleteClassByCode(classCode);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseObjectFactory.toResult("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseObjectFactory.toResult("Susccessfully", HttpStatus.OK);
    }

    @PutMapping("/class")
    public ResponseEntity<Void> updateClass(@RequestParam("classCode") String classCode, @RequestBody Class classContents) {
        classService.updateClass(classCode, classContents);
        return ResponseObjectFactory.toResult("Susccessfully", HttpStatus.OK);
    }

    @GetMapping("/class/users")
    public ResponseEntity<Void> getAllClassMembers(@RequestParam("classCode") String classCode) {
        List<User> listUser = classService.getAllClassMembers(classCode);
        return ResponseObjectFactory.toResult(listUser, HttpStatus.OK);
    }
}
