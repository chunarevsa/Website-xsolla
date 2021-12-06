package com.chunarevsa.Website.service;

import com.chunarevsa.Website.Entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

	private final UserService userService;

	@Autowired
	public AdminService(UserService userService) {
		this.userService = userService;
	}

	public void addMoneyToUser(String username, String amount) {

		User user = userService.findByUsername(username).get();
		if (!validateAmount(amount)) {
			System.err.println("ОШТБКА ВАЛИДАЦИИ"); // TODO: исключение
		}
		if (user.getBalance() == null) {
			user.setBalance("0");
		}

		double oldBalance = Math.round(getInDouble(amount));
		double toAdd = Math.round(getInDouble(user.getBalance()));
		double sum =  Math.round(oldBalance + toAdd);

		user.setBalance(Double.toString(sum));
		userService.saveUser(user);
		System.out.println("Новый баланс :" + user.getBalance() + "$");
	}

	private double getInDouble(String num) {
		return Double.parseDouble(num);
	}

	private boolean validateAmount (String amount) {
		try {
			double value = Double.parseDouble(amount);
			if (value < 0 ) {
				return false;
			}
			return true;

		} catch (Exception e) {
			return false;
		}
		
	}

	
}