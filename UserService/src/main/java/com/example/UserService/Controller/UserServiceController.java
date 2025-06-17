package com.example.UserService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.UserService.Controller.Request.SignUpRequest;
import com.example.UserService.model.User;
import com.example.UserService.service.UserService;

@RestController
@RequestMapping("/api")
public class UserServiceController {

    @Autowired
    private UserService userService;

    @PostMapping("/signupUser")
    public ResponseEntity<String> signupUser(@RequestBody SignUpRequest request) {
    	System.out.println("signup request: " + request);
        String createdUser = userService.SignUpUser(request);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/confirmUser")
    public ResponseEntity<String> confirmUser(@RequestParam String email) {
        String confirmedUser = userService.confirmUser(email);
        return ResponseEntity.ok(confirmedUser);
    }
    
    
    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam long userId)
    {
    	System.out.println("getuser for userId: " + userId);
    	ResponseEntity<User> response = userService.getUser(userId);
    	System.out.println("getuser response: " + response);
    	return response;
    }
    
    @GetMapping("/isUserEligible")
    public ResponseEntity<Boolean> isUserEligible(@RequestParam long userId)
    {
    	boolean eligible = userService.isUserEligible(userId);
    	
    	return ResponseEntity.ok(eligible);
    	
    }

    
    
}
