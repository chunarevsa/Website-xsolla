package com.chunarevsa.Website.service.inter;

import com.chunarevsa.Website.Entity.Currency;
import com.chunarevsa.Website.Exception.DublicateCurrency;
import com.chunarevsa.Website.Exception.FormIsEmpty;
import com.chunarevsa.Website.Exception.NotFound;
import com.chunarevsa.Website.dto.IdDto;
import com.chunarevsa.Website.repo.CurrencyRepository;


public interface CurrencyServiceInterface {
	void currencyIsPresent(long id, CurrencyRepository currencyRepository) throws NotFound;
	void activeValidate (long id, Currency currency) throws NotFound;
	void bodyIsNotEmpty (Currency bodyCurrency) throws FormIsEmpty;
	IdDto getIdByJson (Currency bodyCurrency, CurrencyRepository currencyRepository) throws DublicateCurrency;
	Currency overrideItem (long id, Currency bodyCurrency, CurrencyRepository currencyRepository) throws DublicateCurrency;
} 