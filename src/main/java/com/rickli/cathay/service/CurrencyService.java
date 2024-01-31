package com.rickli.cathay.service;

import com.rickli.cathay.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<Currency> getAllCurrencies();
    Optional<Currency> getCurrencyByCode(String code);

    Currency addCurrency(Currency currency);
    Optional<Currency> updateCurrency(String code, Currency updatedCurrency);
    void deleteCurrency(String code);
}