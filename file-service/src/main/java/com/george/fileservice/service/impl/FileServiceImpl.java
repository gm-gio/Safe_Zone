package com.george.fileservice.service.impl;

import com.george.clients.user.UserClient;
import com.george.clients.user.UserRequest;
import com.george.clients.user.UserResponse;
import com.george.clients.user.UserRole;

import com.george.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UserClient userClient;

    @Override
    public void uploadUsers(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


            for (CSVRecord record : records) {

                String email = record.get("email");


                boolean userExists = checkUserExistsByEmail(email);
                if (userExists) {
                    System.out.println("User with email " + email + " already exists, skipping.");
                    continue;
                }

                String rawPassword = record.get("password");
                String encodedPassword = passwordEncoder.encode(rawPassword);

                UserRequest userRequest = UserRequest.builder()
                        .firstName(record.get("firstName"))
                        .lastName(record.get("lastName"))
                        .email(record.get("email"))
                        .phone(record.get("phone"))
                        .passwordHash(encodedPassword)
                        .userRole(UserRole.valueOf(record.get("role")))
                        .build();


                userClient.register(userRequest);

            }

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    private boolean checkUserExistsByEmail(String email) {
        try {

            ResponseEntity<Boolean> response = userClient.checkUserExistsByEmail(email);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            System.out.println("Error checking user existence by email: " + email);
            return false;
        }
    }


    @Override
    public byte[] generateCsv() {
        List<UserResponse> users = userClient.getUsers().getBody();

        if (users == null || users.isEmpty()) {
            return null;
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Writer writer = new OutputStreamWriter(outputStream)) {


            writer.write("ID,First Name,Last Name,Phone,Email\n");


            for (UserResponse user : users) {
                writer.write(user.getUserId() + ",");
                writer.write(user.getFirstName() + ",");
                writer.write(user.getLastName() + ",");
                writer.write(user.getPhone() + ",");
                writer.write(user.getEmail() + "\n");
            }

            writer.flush();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV file", e);
        }
    }

}
