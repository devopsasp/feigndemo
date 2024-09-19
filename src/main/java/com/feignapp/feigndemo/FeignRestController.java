	package com.feignapp.feigndemo;
	
	import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestLine;
import feign.RequestTemplate;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
	
	@FeignClient(name="authClient")
	interface AuthClient
	{
		 @RequestLine("POST /items")
		Object authenticate(@RequestBody AuthenticateUser authUser);
	}
	
	 class AuthenticateUser {

			private String userName;
			private String password;
			private String role;
			public AuthenticateUser() {
				super();
				// TODO Auto-generated constructor stub
			}
			public String getUserName() {
				return userName;
			}
			public void setUserName(String userName) {
				this.userName = userName;
			}
			public String getPassword() {
				return password;
			}
			public void setPassword(String password) {
				this.password = password;
			}
			public String getRole() {
				return role;
			}
			public void setRole(String role) {
				this.role = role;
			}
			
	    
	}
	@RestController
	public class FeignRestController {
		
	
		@PostMapping("/items")
		public ResponseEntity<?> getItems(@RequestBody AuthenticateUser user)
		{
			AuthClient authClient = Feign.builder()
	                .encoder(new JacksonEncoder())
	                .decoder(new JacksonDecoder())
	                .requestInterceptor(new RequestInterceptor() {
	                    @Override
	                    public void apply(RequestTemplate template) {
	                        template.header("Content-Type", "application/json");
	                    }
	                })
	                .target(AuthClient.class, "http://localhost:8080/order");
	       
	        return  new ResponseEntity<>(authClient.authenticate(user),HttpStatus.OK);
		}
		
		
	}
