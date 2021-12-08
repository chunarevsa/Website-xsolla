package com.chunarevsa.Website.repo;

import com.chunarevsa.Website.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRole (String role); // TODO: mb Optional

} 
