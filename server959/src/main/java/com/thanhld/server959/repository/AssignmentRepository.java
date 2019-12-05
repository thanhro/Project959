package com.thanhld.server959.repository;

import com.thanhld.server959.model.assignment.Assignment;
import com.thanhld.server959.model.classes.Class;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {

    @Query("{'assignmentName':?0, 'classCode':?1}")
    Assignment findByNameAndClassCode(String assignmentName, String classCode);

    @Query(value = "{'link' : ?0}", delete = true)
    void deleteAssignmentByLink(String assignmentLink);

    @Query("{'link':?0}")
    Assignment findByAssignmentLink(String assignmentLink);

    @Query("{'classCode':?0}")
    List<Assignment> findByClassCode(String classCode);

    @Query("{'classCode':{ '$in' : ?0}}")
    List<Assignment> findByClassCode(List<String> classCode);
}
