package com.chunarevsa.Website.repo;

import com.chunarevsa.Website.models.Items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ItemsRepository extends CrudRepository <Items, Long> {
	Page <Items> findAll (Pageable pageable);	
}