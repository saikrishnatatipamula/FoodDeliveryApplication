package com.example.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.UserService.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{
	User findByEmail(String email);
}
