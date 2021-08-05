package com.chunarevsa.Website.controllers;

import com.chunarevsa.Website.Entity.Currency;
import com.chunarevsa.Website.Exception.AllException;
import com.chunarevsa.Website.Exception.NotFound;
import com.chunarevsa.Website.Exception.FormIsEmpty;
import com.chunarevsa.Website.dto.IdByJson;
import com.chunarevsa.Website.dto.Response;
import com.chunarevsa.Website.repo.CurrencyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
public class CurrencyController {
	
	@Autowired

	private CurrencyRepository currencyRepository;
	public CurrencyController (CurrencyRepository currencyRepository) {
		this.currencyRepository = currencyRepository;
	}

	// Получение списка всех Currency с ограничением страницы (10)
	@RequestMapping (path = "/currency", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Currency> currencyFindAll (@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) { 
		// Сортировка по 10 элементов и только со значением active = true
		Page<Currency> pageCurrency =  currencyRepository.findByActive(true, pageable);
		return pageCurrency;
	}

	// Получение по id
	@RequestMapping (path = "/currency/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Currency currencyMethod (@PathVariable(value = "id") long id) throws AllException { 
		// Проверка на наличие 
		Boolean currencyBoolean = currencyRepository.findById(id).isPresent();
		if (!currencyBoolean == true) {
			throw new NotFound(HttpStatus.NOT_FOUND);
		}
		Currency currency = currencyRepository.findById(id).orElseThrow();
		// Вывести только в случае active = true
		if (currency.getActive() == false) {
			throw new NotFound(HttpStatus.NOT_FOUND);
		}
		return currency;
	} 

	// Добавление 
	@PostMapping(value = "/currency", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus (value = HttpStatus.CREATED)	
	public IdByJson createdCurrency (@RequestBody Currency newCurrency) throws AllException {
		if (newCurrency.getCode().isEmpty() == true) {
			throw new FormIsEmpty(HttpStatus.BAD_REQUEST);
		}
		newCurrency.setActive(true);
		currencyRepository.save(newCurrency);
		IdByJson id = new IdByJson(newCurrency.getId());
		return id;
	} 	
				
	 // Изменение
	@PutMapping(value = "/currency/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Currency editCurrency (@PathVariable(value = "id") long id, @RequestBody Currency editCurrency) throws AllException {
		// Проверка на наличие 
		Boolean currencyBoolean = currencyRepository.findById(id).isPresent();
		if (!currencyBoolean == true) {
			throw new NotFound(HttpStatus.NOT_FOUND);
		}
		Currency currency = currencyRepository.findById(id).orElseThrow();
		currency.setCode(editCurrency.getCode());
		if (editCurrency.getCode().isEmpty() == true ) {
			throw new FormIsEmpty(HttpStatus.BAD_REQUEST);
		}
		// Возможность вернуть удалённую (active = false) обратно (active = true)
		currency.setActive(editCurrency.getActive());
		currencyRepository.save(currency);
		return currency;
	} 

   // Удаление
	@DeleteMapping(value = "/currency/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteCurrency (@PathVariable(value = "id") long id) throws AllException {
		// Проверка на наличие
		Boolean currencyBoolean = currencyRepository.findById(id).isPresent();
		if (!currencyBoolean == true) {
			throw new NotFound(HttpStatus.NOT_FOUND);
		}
		Currency currency = currencyRepository.findById(id).orElseThrow();
		if (currency.getActive() == false) {
			throw new NotFound(HttpStatus.NOT_FOUND);
		}
		currency.setActive(false);
		currencyRepository.save(currency);
		Response response = new Response("Успешное удаленние", HttpStatus.OK);
		return response;
	}
}