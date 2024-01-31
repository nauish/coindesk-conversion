package com.rickli.cathay.service;

import com.rickli.cathay.exception.CurrencyNotFoundException;
import com.rickli.cathay.entity.Currency;
import com.rickli.cathay.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }
    public Optional<Currency> getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    public Currency addCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public Optional<Currency> updateCurrency(String code, Currency updatedCurrency) {
        if (code == null || code.trim().isEmpty()) {
            throw new CurrencyNotFoundException("Invalid updated currency data.");
        }

        Optional<Currency> existingCurrency = getCurrencyByCode(code);

        if (existingCurrency.isPresent()) {
            Currency currencyToUpdate = existingCurrency.get();
            currencyToUpdate.setCode(updatedCurrency.getCode());
            currencyToUpdate.setName(updatedCurrency.getName());

            return Optional.of(currencyRepository.save(currencyToUpdate));
        } else {
            throw new CurrencyNotFoundException("Currency with code " + code + " not found.");
        }
    }

    public void deleteCurrency(String code) {
        Optional<Currency> existingCurrency = getCurrencyByCode(code);

        if (existingCurrency.isPresent()) {
            currencyRepository.delete(existingCurrency.get());
        } else {
            throw new CurrencyNotFoundException("Currency with code " + code + " not found.");
        }
    }
}