package com.chunarevsa.Website.controllers;

import com.chunarevsa.Website.models.Merchs;
import com.chunarevsa.Website.repo.MerchsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MerchsController {
	
	@Autowired
	private MerchsRepository merchsRepository;
	public MerchsController (MerchsRepository merchsRepository) {
		this.merchsRepository = merchsRepository;
	}

	 // Получение списка всего мерча
	@RequestMapping (path = "/merchs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<Merchs> merchsMethod () { 
		Iterable<Merchs> merchs = merchsRepository.findAll();
		return merchs;
	} 
	 
	// Добавление мерча
	@PostMapping(value = "/merchs", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus (value = HttpStatus.CREATED)	
	public Merchs createdMerch (@RequestBody Merchs newMerch) {		
		return merchsRepository.save(newMerch);
		} 

	// Изменение
	@PutMapping(value = "/merchs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Merchs editMerch (@PathVariable(value = "id") long id, @RequestBody Merchs merchBody)	{
		Merchs merch = merchsRepository.findById(id).orElseThrow();
		merch.setSku(merchBody.getSku());
		merch.setName(merchBody.getName());
		merch.setDescription(merchBody.getDescription());
		merch.setCost(merchBody.getCost());
		merchsRepository.save(merch);
		return merch;
	} 

	/// Удаление
	@DeleteMapping(value = "/merchs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public long deleteMerch (@PathVariable(value = "id") long id)	{
		merchsRepository.deleteById(id);
		return id;
	}
}
