package com.thanhld.server959.service.point;

import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.point.Point;
import com.thanhld.server959.repository.ClassRepository;
import com.thanhld.server959.repository.PointRespository;
import com.thanhld.server959.security.UserPrincipal;
import com.thanhld.server959.service.assignments.AssignmentService;
import com.thanhld.server959.service.classes.ClassService;
import com.thanhld.server959.service.googledrive.GoogleDriveService;
import com.thanhld.server959.web.rest.errors.BadRequestAlertException;
import com.thanhld.server959.web.rest.errors.ErrorConstants;
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    GoogleDriveService googleDriveService;

    @Autowired
    PointRespository pointRespository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    ClassService classService;

    @Autowired
    AssignmentService assignmentService;

    @Override
    public String getPoint(String userDocLink) {
        Point pointResult = pointRespository.findByLink(userDocLink);
        if (pointResult == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Google drive not found", "Google drive", ErrorConstants.DRIVE_NOT_FOUND);
        }
        return pointRespository.findByLink(userDocLink).getPoint();
    }

    @Override
    public void createPoint(Point point, String classCode) {
        if (point.getLink().isEmpty()) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_PROPERTY_NOT_FOUND, "User link not found in request body", "User link", ErrorConstants.CLASS_NOT_FOUND);
        }
        Point pointObject = pointRespository.findByLink(point.getLink());
//        if (pointObject == null){
//            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Google drive file not found", "Google drive", ErrorConstants.DRIVE_NOT_FOUND);
//        }
        String userId = SecurityUtils.getCurrentUserLogin().get().getId();
        Class classObject = classRepository.findByCodeAndCoach(classCode, userId);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_HAVE_PERMISSION, "User not have permission", "User permission", ErrorConstants.USER_NOT_HAVE_PERMISSION);
        }
        if (validatePoint(point.getPoint()) && pointObject != null) {
            pointObject.setPoint(point.getPoint().trim());
            pointObject.setLink(point.getLink());
            pointRespository.save(pointObject);
            return;
        }
    }

    @Override
    public List<Point> getUserPoints() {
        String userEmail = SecurityUtils.getCurrentUserLogin().get().getEmail();
        return pointRespository.getPointsByWebViewLink(googleDriveService.getAllWebViewLinkByEmail(userEmail));
    }

    @Override
    public List<Point> getUserPointsInClass(String classCode) {

        Class classObject = classService.findByClassCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        String userEmail = SecurityUtils.getCurrentUserLogin().get().getEmail();
        List<Assignment> assignmentList = assignmentService.findByClassCode(classCode);
        List<String> assignmentWebViewLinks = assignmentList.stream().map(link -> link.getLink()).collect(Collectors.toList());

        List<String> userLinks = googleDriveService.getChildrenWebViewLinkByParentWebViewLink(assignmentWebViewLinks, userEmail);
        return pointRespository.getPointsByWebViewLink(userLinks);
    }

    @Override
    public List<Point> getAllUserPointsInClass(String classCode) {
        Class classObject = classService.findByClassCode(classCode);
        if (classObject == null) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Class not found ", "Class", ErrorConstants.CLASS_NOT_FOUND);
        }
        if (classService.isTeacher(classCode)) {
            return pointRespository.findAll();
        }
        throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_HAVE_PERMISSION, "User not have permission", "User permission", ErrorConstants.USER_NOT_HAVE_PERMISSION);
    }

    @Override
    public List<Point> getAllUserPoints() {
        UserPrincipal userObject = SecurityUtils.getCurrentUserLogin().get();
        String userId = userObject.getId();
        List<Class> classes = classService.findByUserId(userId);
        List<Assignment> assignments = assignmentService.findByClassCode(classes);
        return pointRespository.getPointsByWebViewLink(googleDriveService.getChildrenWebViewLinkByParentWebViewLink(assignments.stream().map(assignment -> assignment.getLink()).collect(Collectors.toList())));
    }

    private boolean validatePoint(String point) {
        Integer pointObject;
        try {
            pointObject = Integer.parseInt(point);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FORMAL, "Point not formal", "Point", ErrorConstants.POINT_NOT_FORMAL);
        }

        if (pointObject > 100 || pointObject < 0) {
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FORMAL, "Point not valid", "Point", ErrorConstants.POINT_NOT_FORMAL);
        }
        return true;
    }

}
