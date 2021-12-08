package com.chunarevsa.Website.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.chunarevsa.Website.dto.InventoryUnitDto;
import com.chunarevsa.Website.dto.ItemDto;
import com.chunarevsa.Website.dto.PriceDto;
import com.chunarevsa.Website.entity.Item;
import com.chunarevsa.Website.entity.Price;
import com.chunarevsa.Website.payload.ItemRequest;
import com.chunarevsa.Website.payload.PriceRequest;
import com.chunarevsa.Website.repo.ItemRepository;
import com.chunarevsa.Website.security.jwt.JwtUser;
import com.chunarevsa.Website.service.inter.ItemServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements ItemServiceInterface {

	private final ItemRepository itemRepository;
	private final PriceService priceService;
	private final UserService userService;


	@Autowired
	public ItemService(
				ItemRepository itemRepository,
				PriceService priceService,
				UserService userService) {
		this.itemRepository = itemRepository;
		this.priceService = priceService;
		this.userService = userService;
	}

	/**
	 * Получение Items 
	 * Если ADMIN -> page Items, если USER -> set ItemsDto
	 */
	@Override
	public Object getItems(Pageable pageable, JwtUser jwtUser) {
		List<String> roles = jwtUser.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		if (roles.contains("ROLE_ADMIN")) {
			return getPageItemFromAdmin(pageable);
		} 
		return getItemsDtoFromUser();
	}

	/**
	 * Получение Item
	 * Если ADMIN -> Item, если USER -> ItemDto
	 */
	@Override
	public Object getItem(Long itemId, JwtUser jwtUser) {
		List<String> roles = jwtUser.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		if (roles.contains("ROLE_ADMIN")) {
			return findById(itemId);
		} 
		return getItemDto(itemId);
	}

	/**
	 * Получение у Item списка всех Price
	 * Если ADMIN -> set Pricies, если USER -> set PriciesDto
	 */
	@Override
	public Object getItemPricies (Long itemId, JwtUser jwtUser) {
		List<String> roles = jwtUser.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	
		if (roles.contains("ROLE_ADMIN")) {
			return getItemPriciesFromAdmin(itemId);
		} 
		return getItemPriciesFromUser(itemId);
	}

	/**
	 * Покупка UsetItem (копии Item) за внутреннюю валюту
	 */
	@Override
	public Set<InventoryUnitDto> buyItem(Long itemId, String amountItems, String currencyTitle, JwtUser jwtUser) { //TODO :добвить списание денег

		Item item = findById(itemId).get();
		String cost = priceService.getCostInCurrency(item.getPrices(), currencyTitle);
		return  userService.getSavedInventoryUnit(jwtUser, currencyTitle, cost, amountItems, item);
	}

	/**
	 * Добавление Item
	 */
	@Override
	public Optional<Item> addItem (ItemRequest itemRequest) {
		Item newItem = new Item();
		newItem.setName(itemRequest.getName());
		newItem.setDescription(itemRequest.getDescription());
		newItem.setType(itemRequest.getType());
		newItem.setActive(itemRequest.getActive());
		Set<Price> pricies = priceService.getItemPriciesFromRequest(itemRequest.getPricies());
		newItem.setPrices(pricies);
		priceService.savePricies(newItem.getPrices());
		return saveItem(newItem);
	}

	/**
	 * Изменение Item (без цен)
	 */
	@Override
	public Optional<Item> editItem (long id, ItemRequest itemRequest)  {

		Item item = findById(id).get(); // mb get())
		item.setName(itemRequest.getName());
		item.setType(itemRequest.getType());
		item.setDescription(itemRequest.getDescription());
		item.setActive(itemRequest.isActive());
		saveItem(item);
		return Optional.of(item);
	}

	/**
	 * Изменение и удаление (выключение) Price 
	 */
	@Override
	public Optional<Price> editItemPrice(PriceRequest priceRequest, Long priceId) {
		return priceService.editPrice(priceRequest, priceId);
	}

	/**
	 * Удаление (Выключение) Item
	 */
	@Override
	public void deleteItem(long itemId) {

		Item item = findById(itemId).get();
		priceService.deletePricies(item.getPrices());
		item.setActive(false);
		saveItem(item);

	}

	/**
	 * Получение страницы со всеми Item
	 */
	private Page<Item> getPageItemFromAdmin(Pageable pageable) {
		return findAllItem(pageable);
	}

	/**
	 * Получение всех Price у айтема (влючая выкленные)
	 */
	private Set<Price> getItemPriciesFromAdmin(Long itemId) {
		Item item = findById(itemId).get();
		return item.getPrices();
	}

	/**
	 * Получение списка всех ItemDto
	 */
	private Set<ItemDto> getItemsDtoFromUser() {
		Set<Item> items = findAllByActive(true);
		Set<ItemDto> itemsDto = items.stream().
				map(item -> getItemDto(item.getId()).get()).collect(Collectors.toSet());
		return itemsDto;
	}

	/**
	 * Получение списка всех PriceDto у конкретного Item
	 */
	private Set<PriceDto> getItemPriciesFromUser(Long itemId) {
		Item item = findById(itemId).get();
		return priceService.getItemPriciesDto(item.getPrices());
	}

	/**
	 * Получение ItemDto
	 */
	private Optional<ItemDto> getItemDto(Long id) {
		Item item = findById(id).get();
		return Optional.of(ItemDto.fromUser(item));
	}

	private Set<Item> findAllByActive(boolean active) {
		return itemRepository.findAllByActive(active);
	} 

	private Page<Item> findAllItem(Pageable pageable) {
		return itemRepository.findAll(pageable);
	}

	private Optional<Item> findById (Long id) {
		return itemRepository.findById(id);
	}

	public Set<Item> saveItems(Set<Item> items) {
		return items.stream().map(item -> saveItem(item).get()).collect(Collectors.toSet());
	} 

	private Optional<Item> saveItem(Item item) {
		return Optional.of(itemRepository.save(item));
	}

}
