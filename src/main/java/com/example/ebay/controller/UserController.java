package com.example.ebay.controller;

import com.example.ebay.UserInfo;
import com.example.ebay.service.UserService;
import com.example.ebay.utility.FileAccessUtility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{resource}")
    public ResponseEntity<String> checkAccess(@PathVariable String resource,@RequestHeader("encodedRole") String encodedRole) {
        try {
            UserInfo userInfo = FileAccessUtility.getUserInfoFromHeader(encodedRole);
            if (userService.checkUserAccess(resource,userInfo.getUserId())) {
                return ResponseEntity.ok("Access granted to resource: " + resource);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied to resource: " + resource);
            }
        } catch (JsonParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to grant access.");
        } catch (JsonMappingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to grant access.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        }
    }
    @GetMapping("/encode/{target}")
    public String getBase64String(@PathVariable String target ){
        byte[] bytesToEncode = target.getBytes();
        String encodedString = Base64.getEncoder().encodeToString(bytesToEncode);

        return encodedString;
    }

}
