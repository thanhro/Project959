package com.thanhld.server959.service.point;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.point.Point;
import com.thanhld.server959.repository.ClassRepository;
import com.thanhld.server959.repository.PointRespository;
import com.thanhld.server959.service.googledrive.GoogleDriveService;
import com.thanhld.server959.web.rest.errors.BadRequestAlertException;
import com.thanhld.server959.web.rest.errors.ErrorConstants;
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    GoogleDriveService googleDriveService;

    @Autowired
    PointRespository pointRespository;

    @Autowired
    ClassRepository classRepository;

    @Override
    public String getPoint(String userDocLink) {
        Point pointResult = pointRespository.findByLink(userDocLink);
        if (pointResult == null){
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Google drive not found", "Google drive", ErrorConstants.DRIVE_NOT_FOUND);
        }
        return pointRespository.findByLink(userDocLink).getPoint();
    }

    @Override
    public void createPoint(Point point, String classCode) {
        if (point.getLink().isEmpty()){
            throw new BadRequestAlertException(ErrorConstants.ENTITY_PROPERTY_NOT_FOUND, "User link not found in request body", "User link", ErrorConstants.CLASS_NOT_FOUND);
        }
        Point pointObject = pointRespository.findByLink(point.getLink());
//        if (pointObject == null){
//            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "Google drive file not found", "Google drive", ErrorConstants.DRIVE_NOT_FOUND);
//        }
        String userId = SecurityUtils.getCurrentUserLogin().get().getId();
        Class classObject = classRepository.findByCodeAndCoach(classCode,userId);
        if (classObject == null){
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_HAVE_PERMISSION, "User not have permission", "User permission", ErrorConstants.USER_NOT_HAVE_PERMISSION);
        }
        if (!validatePoint(point.getPoint())){
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_HAVE_PERMISSION, "User not have permission", "User permission", ErrorConstants.USER_NOT_HAVE_PERMISSION);
        }
        if (validatePoint(point.getPoint()) && pointObject!=null){
            pointObject.setPoint(point.getPoint().trim());
            pointObject.setLink(point.getLink());
            pointRespository.save(pointObject);
            return;
        }

        pointRespository.save(point);
    }

    private boolean validatePoint(String point){
        Integer pointObject;
        try {
            pointObject = Integer.parseInt(point);
        }catch (Exception e){
            e.printStackTrace();
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FORMAL, "Point not formal", "Point", ErrorConstants.POINT_NOT_FORMAL);
        }

        if (pointObject > 100 || pointObject<0){
            throw new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FORMAL, "Point not valid", "Point", ErrorConstants.POINT_NOT_FORMAL);
        }
        return true;
    }

}
