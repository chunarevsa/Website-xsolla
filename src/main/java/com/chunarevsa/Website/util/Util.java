package com.chunarevsa.Website.util;

import java.util.UUID;

public class Util {

	private Util () { // TODO: ?
		throw new UnsupportedOperationException("Ошибка создание класса Util");
	}

	public static String generateRandomUuid() {
		return UUID.randomUUID().toString();
	}
	
}
