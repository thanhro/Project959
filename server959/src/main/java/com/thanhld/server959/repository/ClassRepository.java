package com.thanhld.server959.repository;

import com.thanhld.server959.model.classes.Class;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends MongoRepository<Class, String> {

    @Query("{'classCode':?0}")
    Class findByCode(String code);
}
