package com.thanhld.server959.resource;

import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.security.ResponseObjectFactory;
import com.thanhld.server959.service.assignments.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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

    // delete assignment by classCode and assignmentName param
    @DeleteMapping(value = "/assignment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAssignment(@RequestParam("classCode") String classCode, @RequestParam("assignmentName") String assignmentName) {
        assignmentService.deleteAssignment(assignmentName, classCode);
        return ResponseObjectFactory.toResult("Successfully", HttpStatus.OK);
    }

    //update assignment by link (request body: assigmentLink, assignmentName, assignmentDescription, dueDate)
    @PutMapping(value = "/assignment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateAssignment(@RequestParam("classCode") String classCode, @RequestBody Assignment assignment) {
        assignmentService.updateAssignment(assignment, classCode);
        return ResponseObjectFactory.toResult("Successfully", HttpStatus.OK);
    }

    // get all userName by assignment link (@RequestBody required assignmentLink value)
    @GetMapping(value = "/usernames", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getAllUserSharedFileToTeacher(@RequestParam("assignmentName") String assignmentName, @RequestParam("classCode") String classCode) throws Exception {
        Set<String> users = assignmentService.getAllUserSharedFileToTeacher(assignmentName, classCode);
        return ResponseObjectFactory.toResult(users, HttpStatus.OK);
    }

    // get all user docs link by assignment link (@RequestBody required assignmentLink value)
    @GetMapping(value = "/userdocs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getAllUserDocsLinkSharedToTeacher(@RequestParam("assignmentName") String assignmentName, @RequestParam("classCode") String classCode) throws Exception {
        Set<String> users = assignmentService.getAllUserDocsLinkSharedToTeacher(assignmentName, classCode);
        return ResponseObjectFactory.toResult(users, HttpStatus.OK);
    }

    // get all user docs and user name link by assignment link (@RequestBody required assignmentLink value)
    @GetMapping(value = "/userdetails", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getAllUserDocsAndLinkSharedToTeacher(@RequestParam("assignmentName") String assignmentName, @RequestParam("classCode") String classCode) throws Exception {
        Map<String, String> users = assignmentService.getAllUserDocsAndLinkSharedToTeacher(assignmentName, classCode);
        return ResponseObjectFactory.toResult(users, HttpStatus.OK);
    }
}
