package com.thanhld.server959.resource;

import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.security.ResponseObjectFactory;
import com.thanhld.server959.service.assignments.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentResource {

    @Autowired
    AssignmentService assignmentService;

    // create assignment
    @PostMapping(value = "/assignment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAssignment(@RequestParam("classCode") String classCode, @RequestBody Assignment assignmentContents) {
        String folderAssignmentLink = assignmentService.createAssignment(classCode, assignmentContents);
        return ResponseObjectFactory.toResult(folderAssignmentLink, HttpStatus.OK);
    }

    // delete assignment by link
    @DeleteMapping(value = "/assignment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAssignment(@RequestBody Assignment assignment) {
        assignmentService.deleteAssignment(assignment.getLink());
        return ResponseObjectFactory.toResult("Successfully", HttpStatus.OK);
    }

    //update assignment by link (request body: assigmentLink, assignmentName, assignmentDescription, dueDate)
    @PutMapping(value = "/assignment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateAssignment(@RequestBody Assignment assignment) {
        assignmentService.updateAssignment(assignment);
        return ResponseObjectFactory.toResult("Successfully", HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<Set<String>> getAllUserSharedFileToTeacher(@RequestParam("classCode") String classCode) throws Exception {
        Set<String> users = assignmentService.getAllUserSharedFileToTeacher(classCode);
        return ResponseObjectFactory.toResult(users, HttpStatus.OK);
    }
}
