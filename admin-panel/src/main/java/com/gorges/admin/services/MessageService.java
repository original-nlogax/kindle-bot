package com.gorges.admin.services;

import java.util.List;

import com.gorges.admin.models.entities.Message;

public interface MessageService {

    Message findById(Integer id);

    Message update(Message message);

    List<Message> findAll();

}
