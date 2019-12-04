package com.thanhld.server959.repository;

import com.thanhld.server959.model.classes.Class;
import com.thanhld.server959.model.point.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRespository extends MongoRepository<Point, String> {

    @Query("{'link':?0}")
    Point findByLink(String userDocLink);
}
