package com.chunarevsa.Website.controllers;

import java.util.ArrayList;
import java.util.Optional;

import com.chunarevsa.Website.models.Currencies;
import com.chunarevsa.Website.repo.СurrenciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class СurrenciesController {
	
	@Autowired
	private СurrenciesRepository currenciesRepository;

 	@GetMapping ("/currencies")
 	public String currenciesMain (Model model) {
		 Iterable<Currencies> currencies = currenciesRepository.findAll();
		 model.addAttribute("currencies", currencies); // Массив данных из таблицы
		 return "currencies-main";

 	}

	 @GetMapping ("/currencies/add")
	 public String currenciesAdd (Model model) {
		 return "currencies-add";
	 }

	 @PostMapping ("/currencies/add")
	 public String currenciesPostAdd (@RequestParam String name, @RequestParam String description, @RequestParam int cost, Model model) {
		 Currencies сurrency = new Currencies(name, description, cost);
		 сurrency.setType("Валюта");
		 currenciesRepository.save(сurrency);
		 return "redirect:/currencies";
	 }

	 // Обработчки Динамической ссылки
	@GetMapping ("/currencies/{id}") 
	public String currenciesDetails (@PathVariable(value = "id") long id, Model model) {
		 if (!currenciesRepository.existsById(id)){ 
		  return "redirect:/currencies";
		 } 
		 Optional<Currencies> сurrency = currenciesRepository.findById(id);
		 ArrayList<Currencies> res = new ArrayList<>();
		 сurrency.ifPresent(res::add);
		 model.addAttribute("currency", res);
		 return "currencies-details";
	}

	// Изменение
	@GetMapping ("/currencies/{id}/edit") 
	public String currenciesEdit (@PathVariable(value = "id") long id, Model model) {
		 if (!currenciesRepository.existsById(id)){ 
		  return "redirect:/currencies";
		 } 
		 Optional<Currencies> currency = currenciesRepository.findById(id);
		 ArrayList<Currencies> res = new ArrayList<>();
		 currency.ifPresent(res::add);
		 model.addAttribute("currency", res);
		 return "currencies-edit";
	}
  @PostMapping ("/currencies/{id}/edit") 
  public String currenciesPostUpdate (@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String description, @RequestParam int cost, Model model) {
	  Currencies currency = currenciesRepository.findById(id).orElseThrow();
	  currency.setName(name);
	  currency.setDescription(description);
	  currency.setCost(cost);
	  currenciesRepository.save(currency);
	  return "redirect:/currencies";
  }

  // Удаление 
	@PostMapping ("/currencies/{id}/remove") 
	public String currenciesPostDelete (@PathVariable(value = "id") long id, Model model) {
		Currencies currency = currenciesRepository.findById(id).orElseThrow();
		currenciesRepository.delete(currency);
		return "redirect:/currencies";
	}
}
