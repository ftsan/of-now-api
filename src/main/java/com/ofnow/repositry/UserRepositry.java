package com.ofnow.repositry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ofnow.domain.User;

public interface UserRepositry extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.inOffice = true ORDER BY u.updateTime")
	public Page<User> findInOffice(Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.uuid = :uuid")
	public User findByUUID(@Param("uuid") String uuid);

}