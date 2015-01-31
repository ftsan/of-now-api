package com.ofnow.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import lombok.Data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ofnow.App;
import com.ofnow.domain.User;
import com.ofnow.repositry.UserRepositry;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0", "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE"})
public class UserControllerTest {
	@Autowired
	UserRepositry userRepositry;
	
	@Value("${local.server.port}")
	int port;
	
	String endPoint;
	RestTemplate restTemplate = new TestRestTemplate();
	User user1;
	User user2;
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Page<T> {
		private List<T> content;
		private int numberOfElements;
	}
	
	@Before
	public void setup() {
		userRepositry.deleteAll();
		Date d1 = new GregorianCalendar(2015, 1, 3).getTime();
		Date d2 = new GregorianCalendar(2015, 1, 2).getTime();
		user1 = new User("test", "testUuid", "test@mail.com", true, d1, d1);
		user2 = new User("test2", "testUuid2", "test2@mail.com", true, d2, d2);
		
		userRepositry.save(Arrays.asList(user1, user2));
		endPoint = "http://localhost:" + port + "/api/users";
	}
	
	@Test
	public void testGetInOfficeUser() throws Exception {
		ResponseEntity<Page<User>> response =
				restTemplate.exchange(
						endPoint, HttpMethod.GET, null, new ParameterizedTypeReference<Page<User>>() {});
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getNumberOfElements(), is(2));
		
		User u1 = response.getBody().getContent().get(0);
		assertThat(u1.getUuid(), is(user1.getUuid()));
		assertThat(u1.getName(), is(user1.getName()));
		assertThat(u1.getMail(), is(user1.getMail()));
		
		User u2 = response.getBody().getContent().get(1);
		assertThat(u2.getUuid(), is(user2.getUuid()));
		assertThat(u2.getName(), is(user2.getName()));
		assertThat(u2.getMail(), is(user2.getMail()));
	}
	
	@Test
	public void testCreate() {
		String name = "test";
		String mail = "test@mail.com";
		boolean inOffice = true;
		User u = new User(null, name, mail, true, null, null);
		ResponseEntity<User> response = restTemplate.exchange(endPoint, HttpMethod.POST, new HttpEntity<>(u), User.class);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		User created = response.getBody();
		assertThat(created.getName(), is(name));
		assertThat(created.getMail(), is(mail));
		assertThat(created.isInOffice(), is(inOffice));
		assertThat(restTemplate.exchange(
				endPoint, HttpMethod.GET, null, 
				new ParameterizedTypeReference<Page<User>>() {})
				.getBody().getNumberOfElements(), is(3));
	}
	
	@Test
	public void testGetByUuid() {
		ResponseEntity<Page<User>> users =
				restTemplate.exchange(
						endPoint, HttpMethod.GET, null, new ParameterizedTypeReference<Page<User>>() {});
		User u = users.getBody().getContent().get(0);
		System.out.println("uuid : " + u.getUuid());
		ResponseEntity<Page<User>> response = restTemplate.exchange(endPoint,
				HttpMethod.GET, null, new ParameterizedTypeReference<Page<User>>() {
				}, Collections.singletonMap("uuid", u.getUuid()));
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getContent().get(0).getUuid(), is(u.getUuid()));
		assertThat(response.getBody().getContent().get(0).getName(), is(u.getName()));
	}
	
	@Test
	public void testUpdate() {
		ResponseEntity<Page<User>> users =
				restTemplate.exchange(
						endPoint, HttpMethod.GET, null, new ParameterizedTypeReference<Page<User>>() {});
		User u = users.getBody().getContent().get(0);
		u.setName("test" + new Random().nextInt());
		ResponseEntity<User> response = restTemplate.exchange(endPoint, HttpMethod.PUT, new HttpEntity<>(u), User.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
//		assertThat(response.getBody().getContent().get(0).getName(), is(u.getName()));
	}

}
