package com.example.ebay.utility;


import com.example.ebay.UserInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class FileAccessUtility {

    private static final String FILE_PATH = "user_access.txt";

    public static boolean writeAccessToFile(Map<String, Object> userData) {
        File file = new File(FILE_PATH);
        List<String> fileContent = new ArrayList<>();
        boolean found = false;
        try {
            // Ensure the file exists
            file.createNewFile();
            // Read the existing file and collect all lines except the one with the same userId
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String currentLine;
                String userId = String.valueOf(userData.get("userId"));
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.contains("userId=" + userId )) {
                        found = true; // Found the userId, do not add this line back
                    } else {
                        fileContent.add(currentLine); // Add line if it's not the one we're updating
                    }
                }
            }
            // Append the new userData in JSON format (assuming userData is a map in JSON-like format)
            fileContent.add(userData.toString());
            // Rewrite the file with the new content
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : fileContent) {
                    writer.write(line + System.lineSeparator());
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkAccessInFile(String resource,String role) {
        File file = new File(FILE_PATH);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming the format stored in the file is user-friendly for parsing
                String[] parts = line.split("endpoint");
                if (parts[0].contains(role) && parts[1].contains(resource)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UserInfo getUserInfoFromHeader(String userInfo) throws Exception{
        byte[] decodedBytes = Base64.getDecoder().decode(userInfo);
        String json = new String(decodedBytes);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, UserInfo.class);
    }
}

