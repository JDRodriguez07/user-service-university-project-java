package edu.university.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.university.user_service.dto.UserFullResponseDTO;
import edu.university.user_service.service.UserSearchService;

@RestController
@RequestMapping("/users/search")
public class UserSearchController {

    @Autowired
    private UserSearchService userSearchService;

    @GetMapping
    public ResponseEntity<UserFullResponseDTO> search(@RequestParam String value) {
        return ResponseEntity.ok(userSearchService.search(value));
    }
}
