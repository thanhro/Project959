package com.thanhld.server959.repository;

import com.thanhld.server959.model.point.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRespository extends MongoRepository<Point, String> {

    @Query("{'link':?0}")
    Point findByLink(String userDocLink);

    @Query("{'link':{ '$in' :?0 }}")
    List<Point> getPointsByWebViewLink(List<String> webViewLink);

    @Query("{'link':{ '$in' :?0 }}")
    List<Point> getPointsInClassByWebViewLink(List<String> allWebViewLinkByEmail, String classCode);
}
