package com.chunarevsa.Website.repo;

import java.util.Set;

import com.chunarevsa.Website.entity.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

	Set<Item> findAllByActive(boolean active);

	Page<Item> findAll (Pageable pageable);

}
