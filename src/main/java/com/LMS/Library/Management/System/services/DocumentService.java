package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.DocumentDao;
import com.LMS.Library.Management.System.dto.UserDocumentDTO;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.entities.UserDocument;
import com.LMS.Library.Management.System.enums.DocumentType;
import com.LMS.Library.Management.System.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class DocumentService {
    @Autowired
    private UserService userService;

    @Autowired
    private DocumentDao documentDao;

    private final String UPLOAD_DIRECTORY = "uploads/documents";

    public void upload(UserDocumentDTO userDocumentDTO, Integer userId) {
        MultipartFile file = userDocumentDTO.getMultipartFile();

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        try {
            User user = userService.findUserById(userId);
            if (user == null) throw new RuntimeException("User Not Found");

            File directory = new File(UPLOAD_DIRECTORY);

            if (!directory.exists()) directory.mkdirs();

            // unique file name
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIRECTORY, fileName);

            // Save File
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Saving in db
            UserDocument document = UserDocument.builder()
                    .documentType(userDocumentDTO.getDocumentType())
                    .fileName(fileName)
                    .filePath(path.toString())
                    .documentNumber(userDocumentDTO.getDocumentNumber())
                    .user(user)
                    .status(Status.PENDING)
                    .build();

            documentDao.save(document);


        } catch (Exception e) {
            throw new RuntimeException("Document Upload Failed");
        }

    }
}
