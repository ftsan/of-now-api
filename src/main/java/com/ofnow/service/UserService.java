package com.ofnow.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ofnow.domain.User;
import com.ofnow.repositry.UserRepositry;
import com.ofnow.util.UUIDGenarator;

@Service
@Transactional
public class UserService {
	@Autowired
	UserRepositry repositry;
	
	public Page<User> findInOffice(Pageable pageable) {
		return repositry.findInOffice(pageable);
	}
	
	public User create(User user) {
		user.setUuid(generateUUID());
		Date now = new Date();
		user.setCreateTime(now);
		user.setUpdateTime(now);
		return repositry.save(user);
	}
	
	public User update(User user) {
		return repositry.save(user);
	}
	
	public User findByUUID(String uuid) {
		return repositry.findByUUID(uuid);
	}
	/**
	 * @return 使用されていないuuid
	 */
	private String generateUUID() {
		while(true) {
			String uuid = UUIDGenarator.randomUUID();
			Optional<User> user = Optional.ofNullable(repositry.findByUUID(uuid));
			if(!user.isPresent()) {
				return uuid;
			}
		}
	}
	

}
