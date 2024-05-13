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

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<String> addUserAccess(@RequestBody Map<String, Object> userData,@RequestHeader("encodedRole") String encodedRole) {
        try {
            UserInfo userInfo = FileAccessUtility.getUserInfoFromHeader(encodedRole);
            if(!"admin".equals(userInfo.getRole().toLowerCase())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no authority for common users.");
            }
            boolean isAdded = userService.addUserAccess(userData);
            if (isAdded) {
                return ResponseEntity.ok("Access granted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to grant access.");
            }
        } catch (JsonParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to grant access.");
        } catch (JsonMappingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to grant access.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        }
    }

}
