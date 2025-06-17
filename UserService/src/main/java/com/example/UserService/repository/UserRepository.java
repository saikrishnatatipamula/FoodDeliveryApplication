package com.example.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.UserService.model.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>
{
	User findByEmail(String email);
	User findUserById(long id);
}
