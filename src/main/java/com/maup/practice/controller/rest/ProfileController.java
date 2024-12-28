package com.maup.practice.controller.rest;

import com.maup.practice.dto.UserProfileDTO;
import com.maup.practice.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserFacade userFacade;

    @Autowired
    public ProfileController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/getUserProfile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        return ResponseEntity.ok(userFacade.getUserProfile());
    }
}
