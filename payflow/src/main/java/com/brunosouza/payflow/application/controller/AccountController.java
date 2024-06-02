package com.brunosouza.payflow.application.controller;

import com.brunosouza.payflow.domain.account.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
//        List<Account> accounts =
        return ResponseEntity.ok(null);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty");
        }

        return ResponseEntity.ok("File " + file.getOriginalFilename() + " was sent success");
    }
}
