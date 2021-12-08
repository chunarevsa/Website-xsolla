package com.chunarevsa.Website.controllers;

import javax.validation.Valid;

import com.chunarevsa.Website.exception.AllException;
import com.chunarevsa.Website.payload.ItemRequest;
import com.chunarevsa.Website.payload.PriceRequest;
import com.chunarevsa.Website.security.jwt.JwtUser;
import com.chunarevsa.Website.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Товар который можно приобрести за внутреннюю валюту
 * Например: броня, стрелы, скин... что угодно.
 * Пользователь приобретает копию товара - UserItem
 * UserItem добавляется в одну из ячеек в инвентаре пользователя
 * 
 */
@RestController
@RequestMapping("/item")
public class ItemController {
	
	private final ItemService itemService;
	
	@Autowired
	public ItemController (ItemService itemService) {
			this.itemService = itemService;
	}

	/**
	 * Получение Items 
	 * Если ADMIN -> page Items, если USER -> set ItemsDto
	 */
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getItems(@PageableDefault Pageable pageable, 
				@AuthenticationPrincipal JwtUser jwtUser) { 

		return ResponseEntity.ok().body(itemService.getItems(pageable, jwtUser));
	} 

	/**
	 * Получение Item
	 * Если ADMIN -> Item, если USER -> ItemDto
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getItem (@PathVariable(value = "id") Long id, 
			@AuthenticationPrincipal JwtUser jwtUser) throws AllException {

		return ResponseEntity.ok().body(itemService.getItem(id, jwtUser));
	}

	/**
	 * Получение у Item списка всех Price
	 * Если ADMIN -> set Pricies, если USER -> set PriciesDto
	 * @param itemId
	 * @param jwtUser
	 */
	@GetMapping("/{id}/pricies")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getItemPricies (@PathVariable(value = "id") Long itemId, 
					@AuthenticationPrincipal JwtUser jwtUser) throws AllException {
	
		return ResponseEntity.ok().body(itemService.getItemPricies(itemId, jwtUser));
	}

	/**
	 * Покупка UsetItem (копии Item) за внутреннюю валюту
	 * @param itemId
	 * @param amountitem
	 * @param currencytitle
	 * @param jwtUser
	 */
	@PostMapping("/{id}/buy")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity buyItem (@PathVariable(value = "id") Long itemId,
					@RequestParam String amountitem,
					@RequestParam String currencytitle,
					@AuthenticationPrincipal JwtUser jwtUser) {
		
		return ResponseEntity.ok().body(itemService.buyItem(itemId, amountitem, currencytitle, jwtUser));
	} 

	/**
	 * Добавление Item
	 * @param itemRequest
	 */
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity addItem (@Valid @RequestBody ItemRequest itemRequest) throws AllException {

		return itemService.addItem(itemRequest) // TODO: исключение
				.map(item -> ResponseEntity.ok().body(item)).orElseThrow();
	} 	
	
	 /**
	  * Изменение Item (без цен)
	  * @param id
	  * @param itemRequest
	  */
	@PutMapping("/{id}/edit")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity editItem (@PathVariable(value = "id") Long id, 
				@Valid @RequestBody ItemRequest itemRequest) throws AllException {
		
		return ResponseEntity.ok(itemService.editItem(id, itemRequest)); 
	}

	/**
	 * Изменение и удаление (выключение) Price 
	 */
	@PutMapping("/price/{priceId}/edit")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity editItemPrice (@PathVariable(value = "priceId") Long priceId,
				@Valid @RequestBody PriceRequest priceRequest) throws AllException {
		return ResponseEntity.ok().body(itemService.editItemPrice(priceRequest, priceId));
	} 

	/**
	 * Удаление (Выключение) Item
	 * @param id
	 */
	@DeleteMapping("/{id}/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity deleteItem (@PathVariable(value = "id") Long id) throws AllException {
		itemService.deleteItem(id);
		return ResponseEntity.ok().body("Item " + id + " был удален");
	}

}
