package com.chunarevsa.Website.service;

import java.util.*;

import com.chunarevsa.Website.Entity.Role;
import com.chunarevsa.Website.Entity.User;
import com.chunarevsa.Website.Entity.payload.RegistrationRequest;
import com.chunarevsa.Website.repo.UserRepository;
import com.chunarevsa.Website.service.inter.UserServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

// Добавить обработку исключений (доделать)
@Service
@Slf4j
public class UserService implements UserServiceInterface{

	private final UserRepository userRepository;
	//private final UserValid userValid;
	private final RoleService roleService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserService(
				UserRepository userRepository,
				//UserValid userValid,
				RoleService roleService,
				BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		//this.userValid = userValid;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override 
	public User addNewUser (RegistrationRequest registerRequest) {

		User newUser = new User();
		Boolean isAdmin = registerRequest.getRegisterAsAdmin();
		newUser.setEmail(registerRequest.getEmail());
		// Кодирование пароля для хранения в БД
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setUsername(registerRequest.getUsername());
		newUser.addRoles(getRoles(isAdmin));
		newUser.setActive(true);
		newUser.setIsEmailVerified(false);
		log.info("IN register - user: {} seccesfully registred", newUser);
		return newUser;
	}

	public User save(User user) {
		return userRepository.save(user);
  	}

	private Set<Role> getRoles(Boolean isAdmin) {
		Set<Role> roles = new HashSet<>(roleService.findAll());
		// Проврка может ли быть новый пользователь админом 
		if (!isAdmin) {
			roles.removeIf(Role::isAdminRole);
	  }
		return roles;
	}

	@Override
	public List<User> getAll() {
		List<User> result = userRepository.findAll();
		log.info("IN getAll - {} users found", result.size());
		return result;
	}

	@Override
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		log.info("IN findByUsername - user: {} found by username: {}", username);
		return user;
	}

	@Override
	public User findById(Long id) {

		User user = userRepository.findById(id).orElse(null);
		if (user == null) {
			log.warn("IN findById - no user found by id: {}", id);
			return null;
	  }

	  log.info("IN findById - found user by id: {}", id);
		return user;
	}

	// Переделать на выключение (доделать)
	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
		log.info("IN delete - user with id: {} successfully deleted");
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
} 
