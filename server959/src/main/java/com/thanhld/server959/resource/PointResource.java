package com.thanhld.server959.resource;

import com.thanhld.server959.model.point.Point;
import com.thanhld.server959.model.security.ResponseObjectFactory;
import com.thanhld.server959.service.point.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point")
public class PointResource {

    @Autowired
    PointService pointService;

    // create or update point by requestBody (link, point)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createOrUpdatePoint(@RequestParam("classCode") String classCode, @RequestBody Point point) {
        pointService.createPoint(point, classCode);
        return ResponseObjectFactory.toResult("Successfully", HttpStatus.OK);
    }

    // get point by requestParam (link)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPoint(@RequestParam("link") String link) {
        String pointValue = pointService.getPoint(link);
        return ResponseObjectFactory.toResult(pointValue, HttpStatus.OK);
    }

    // get all assignment point user(student role)
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserPointAssignment() {
        return ResponseObjectFactory.toResult(pointService.getUserPoints(), HttpStatus.OK);
    }

    // get all assignment point user in class(teacher role)
    @GetMapping(value = "/userpoints/class", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllUserPointsAssignmentInClass(@RequestParam("classCode") String classCode) {
        return ResponseObjectFactory.toResult(pointService.getAllUserPointsInClass(classCode), HttpStatus.OK);
    }

    // get all assignment point user in class(student role)
    @GetMapping(value = "/user/class", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserPointsAssignmentInClass(@RequestParam("classCode") String classCode) {
        return ResponseObjectFactory.toResult(pointService.getUserPointsInClass(classCode), HttpStatus.OK);
    }

    // get all assignment point user(teacher role) all class
    @GetMapping(value = "/userpoints/class/all", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllUserPointsAssignment() {
        return ResponseObjectFactory.toResult(pointService.getAllUserPoints(), HttpStatus.OK);
    }
}
