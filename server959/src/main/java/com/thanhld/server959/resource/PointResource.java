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

    // create or upodate point by requestBody (link, point)
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
}
