package com.thanhld.server959.resource;

import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.security.ResponseObjectFactory;
import com.thanhld.server959.service.assignments.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentResource {

    @Autowired
    AssignmentService assignmentService;

    @PostMapping(value = "/assignment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createClass(@RequestBody Assignment assignmentContents) {
        String folderAssignmentLink = assignmentService.createAssignment(assignmentContents);
        return ResponseObjectFactory.toResult(folderAssignmentLink, HttpStatus.OK);
    }
}
