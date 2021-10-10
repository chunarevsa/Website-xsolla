package com.chunarevsa.Website.service;

import com.chunarevsa.Website.Entity.Item;
import com.chunarevsa.Website.Exception.FormIsEmpty;
import com.chunarevsa.Website.Exception.NotFound;
import com.chunarevsa.Website.dto.IdByJson;
import com.chunarevsa.Website.repo.ItemRepository;
import com.chunarevsa.Website.service.inter.ItemServiceInterface;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements ItemServiceInterface{

	// Проверка на наличие 
	@Override
	public void itemIsPresent (long id, ItemRepository itemRepository) throws NotFound{
		Boolean item = itemRepository.findById(id).isPresent();
		if (item == false) {
			throw new NotFound(HttpStatus.NOT_FOUND);
		}	 
	}

	// Проверка не выключен ли active = true
	@Override
	public void activeValidate (long id, Item item) throws NotFound {
		if (item.getActive() == false) {
			throw new NotFound(HttpStatus.NOT_FOUND);
		}
	}

	// Проверка на незаполеннные данные
	@Override
	public void bodyIsNotEmpty (Item bodyItem) throws FormIsEmpty {
		if (
		bodyItem.getName().isEmpty() == true || 
		bodyItem.getSku().isEmpty() == true || 
		bodyItem.getType().isEmpty() == true || 
		bodyItem.getDescription().isEmpty() == true) {
			throw new FormIsEmpty(HttpStatus.BAD_REQUEST);
		}
	}

	// Представление Id в JSON
	@Override
	public IdByJson getIdByJson (Item bodyItem, ItemRepository itemRepository) {
		itemRepository.save(bodyItem);
		IdByJson idByJson = new IdByJson(bodyItem.getId());
		return idByJson;
	}

	// Запись параметров
	@Override
	public Item overrideItem (long id, Item bodyItem, ItemRepository itemRepository) {
		Item item = itemRepository.findById(id).orElseThrow();
		item.setSku(bodyItem.getSku());
		item.setName(bodyItem.getName());
		item.setType(bodyItem.getType());
		item.setDescription(bodyItem.getDescription());
		// Возможность вернуть удалённый (active = false) обратно (active = true)
		item.setActive(bodyItem.getActive());
		item.setPrices(bodyItem.getPrices());
		return item;
	}
	
	
}