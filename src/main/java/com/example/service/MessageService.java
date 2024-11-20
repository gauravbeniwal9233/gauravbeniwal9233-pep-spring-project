package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import antlr.debug.MessageAdapter;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage (Message message) {
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new IllegalArgumentException("text can't be blank !!");
        }
        if (message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("text can't be unser 255 characaters !!");
        }
        if (message.getPostedBy() == null || !accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("postedBy doesn't exist !!");
        }

        return messageRepository.save(message);

    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public boolean deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public int updateMessageById(Integer messageId, String newMessafgeText) {
        if (newMessafgeText == null || newMessafgeText.trim().isEmpty()) {
            throw new IllegalArgumentException("text can't be blank !!");
        }
        if (newMessafgeText.length() > 255) {
            throw new IllegalArgumentException("text must be under 255 char !!");
        }

        Optional<Message> optionalMessage = messageRepository.findById((messageId));
        if (optionalMessage.isEmpty()) {
            throw new IllegalArgumentException("msg w/ given id not exist !!");
        }

        Message existingMessage = optionalMessage.get();
        existingMessage.setMessageText(newMessafgeText);
        messageRepository.save(existingMessage);
        return 1;
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findAllByPostedBy(accountId);
    }
}
