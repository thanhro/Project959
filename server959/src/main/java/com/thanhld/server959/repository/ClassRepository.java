package com.thanhld.server959.repository;

import com.thanhld.server959.model.classes.Class;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends MongoRepository<Class, String> {

    @Query("{'classCode':?0}")
    Class findByCode(String code);

    @Query("{'className' : ?0 ,'coach' : ?1}")
    Class findByNameAndCoach(String classCode, String coach);

    @Query("{'classCode' : ?0 ,'coach' : ?1}")
    Class findByCodeAndCoach(String classCode, String coach);

    @Query("{'coach' : ?0}")
    List<Class> findByCoach(String userId);
}
