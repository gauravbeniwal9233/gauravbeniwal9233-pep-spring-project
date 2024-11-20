package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired 
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount (@RequestBody Account account) {
        try {
            Account registeredAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(registeredAccount); //200
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Username already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); //409
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); //400
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody Account account) {
        try {
            Account loginAccount = accountService.authenticate(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(loginAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @PostMapping("/messages")
    public ResponseEntity<?> createMessage (@RequestBody Message message) {
        try {
            Message createedMessage = messageService.createMessage(message);
            return ResponseEntity.ok(createedMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        return message.map(ResponseEntity::ok).orElse(ResponseEntity.ok(null));
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessageById(@PathVariable("message_id")Integer messageId) {
        boolean isDeleted = messageService.deleteMessageById(messageId);
        if (isDeleted) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> updateMessageById(@PathVariable("message_id")Integer messageId, @RequestBody Message updatMessage) {
        try {
            int rowsUpdated = messageService.updateMessageById(messageId, updatMessage.getMessageText());
            return ResponseEntity.ok(rowsUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}