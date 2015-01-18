package com.ofnow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.ofnow.repositry.UserRepositry;

@EnableAutoConfiguration
@ComponentScan
public class App {
	@Autowired
	UserRepositry userRepositry;
	
    public static void main( String[] args ) {
    	SpringApplication.run(App.class, args);
    }
}
