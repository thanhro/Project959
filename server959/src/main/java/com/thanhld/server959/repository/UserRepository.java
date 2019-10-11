package com.thanhld.server959.repository;

import com.thanhld.server959.model.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);
}
