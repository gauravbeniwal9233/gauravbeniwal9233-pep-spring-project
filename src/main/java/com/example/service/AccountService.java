package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(String username, String password) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be balnk");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long");
        }
        if (accountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Account newAccount = new Account(username, password);
        return accountRepository.save(newAccount);
    }

    public Account authenticate(String username, String password) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()){
            throw new IllegalArgumentException("Username and password can't be blank !!");
        }

        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Invalid username or passwrd !!"));

        if (!account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or pass !!");
        }

        return account;
    }
}
