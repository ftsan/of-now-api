package com.ofnow.api;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ofnow.domain.User;
import com.ofnow.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {
	@Autowired
	UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public Page<User> getInOfficeUser(@PageableDefault Pageable pageable) {
		return userService.findInOffice(pageable);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> create(@RequestBody User user, UriComponentsBuilder builder) {
		User created = userService.create(user);
		URI location = builder.path("api/users/{uuid}").buildAndExpand(created.getUuid()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		return new ResponseEntity<User>(created, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "{uuid}", method = RequestMethod.GET)
	public User get(String uuid) {
		return userService.findByUuid(uuid);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public User update(@RequestBody User data) {
		Optional<User> value = Optional.of(userService.findByUuid(data.getUuid()));
		if(!value.isPresent()) {
			return null;
		}
		User user = value.get();
		user.setName(data.getName());
		user.setInOffice(data.isInOffice());
		user.setMail(data.getMail());
		user.setUpdateTime(new Date());
		return userService.update(data);
	}

}
