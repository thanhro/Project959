package com.thanhld.server959.service.point;

import com.thanhld.server959.model.point.Point;

public interface PointService {
    String getPoint(String userDocLink);

    void createPoint(Point userDocLink, String classCode);
}
