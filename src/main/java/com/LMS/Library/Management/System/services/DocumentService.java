package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.DocumentDao;
import com.LMS.Library.Management.System.dto.UserDocumentDTO;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.entities.UserDocument;
import com.LMS.Library.Management.System.enums.Status;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private UserService userService;

    @Autowired
    private DocumentDao documentDao;

    private final String UPLOAD_DIRECTORY = "uploads/documents";

    @Transactional
    public void upload(UserDocumentDTO userDocumentDTO, Integer userId) {
        MultipartFile file = userDocumentDTO.getMultipartFile();

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Upload the Image");
        }

        User user = userService.findUserById(userId);

        if (user == null) throw new RuntimeException("User Not Found");

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File directory = new File(UPLOAD_DIRECTORY);

        if (!directory.exists()) directory.mkdirs();

        Path path = Paths.get(UPLOAD_DIRECTORY, fileName);

        Optional<UserDocument> document = documentDao.findByUserAndDocumentType(user, userDocumentDTO.getDocumentType());
        try {

            if (document.isPresent()) {
                UserDocument userDocument = document.get();

                // Deleting the file
                Files.deleteIfExists(Paths.get(userDocument.getFilePath()));
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                // Updating
                userDocument.setFileName(fileName);
                userDocument.setFilePath(path.toString());
                userDocument.setFileSize(file.getSize());


                documentDao.save(userDocument);

            } else {
                // Saving new document in the db
                UserDocument newDocument = UserDocument.builder()
                        .documentType(userDocumentDTO.getDocumentType())
                        .fileName(fileName)
                        .filePath(path.toString())
                        .documentNumber(userDocumentDTO.getDocumentNumber())
                        .user(user)
                        .status(Status.PENDING)
                        .originalFileName(file.getOriginalFilename())
                        .fileSize(file.getSize())
                        .build();
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                documentDao.save(newDocument);
            }



        } catch (IOException e) {
            throw new RuntimeException("Document Upload Failed");
        }

    }
}
