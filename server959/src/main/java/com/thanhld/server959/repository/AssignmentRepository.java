package com.thanhld.server959.repository;

import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.classes.Class;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {
    @Query("{'assignmentName':?0}")
    Class findByName(String assignmentName);

    @Query("{'assignmentName':?0, 'classCode':?1}")
    Class findByNameAndClassCode(String assignmentName, String classCode);

    @Query("{'classCode':?0}")
    Class findByCode(String classCode);

    @Query(value = "{'link' : ?0}", delete = true)
    void deleteAssignmentByLink(String assignmentLink);

    @Query("{'link':?0}")
    Assignment findByAssignmentLink(String assignmentLink);
}
