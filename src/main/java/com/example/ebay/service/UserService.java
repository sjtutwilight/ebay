package com.example.ebay.service;

import com.example.ebay.utility.FileAccessUtility;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    public boolean addUserAccess(Map<String, Object> userData) {
        return FileAccessUtility.writeAccessToFile(userData);
    }

    public boolean checkUserAccess(String resource,String role) {
        // Read the file and check if the resource is accessible to the user
        return FileAccessUtility.checkAccessInFile(resource,role);
    }
}
