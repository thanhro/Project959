package com.thanhld.server959.service.point;

import com.thanhld.server959.model.point.Point;

import java.util.List;

public interface PointService {
    String getPoint(String userDocLink);

    void createPoint(Point userDocLink, String classCode);

    List<Point> getUserPoints();

    List<Point> getUserPointsInClass(String classCode);

    List<Point> getAllUserPointsInClass(String classCode);

    List<Point> getAllUserPoints();
}
