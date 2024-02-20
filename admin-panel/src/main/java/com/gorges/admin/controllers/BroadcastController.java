package com.gorges.admin.controllers;

import com.gorges.admin.services.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/broadcasts")
public class BroadcastController {

    private final BroadcastService broadcastService;

    @Autowired
    public BroadcastController(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @GetMapping("/add")
    public String showAddBroadcast() {
        return "main/broadcasts/add";
    }

    @PostMapping("/send")
    public String sendBroadcast(@RequestParam String text) {
        broadcastService.send(text);
        return "redirect:/broadcasts/add?send";
    }

}
