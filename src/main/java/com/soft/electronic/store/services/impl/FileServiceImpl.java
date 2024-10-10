package com.soft.electronic.store.services.impl;

import com.soft.electronic.store.exceptions.BadApiRequestException;
import com.soft.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService
{
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException { //to upload image to path

        String originalFilename = file.getOriginalFilename();
        logger.info(originalFilename);
        String fileName = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName+extension;
        String fullPathWithFileName = path+fileNameWithExtension;

        logger.info("Full Image Path :{} ",fullPathWithFileName);
        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){
            //file save
            logger.info("Extension :{} ",extension);

            File folder = new File(path);
            if (!folder.exists()){

                //create folder
                folder.mkdirs(); //for nested folder creation

                //upload
                Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
                return fileNameWithExtension;
            } else if (folder.exists()) {
                Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
                return fileNameWithExtension;
            }
        } else {
            throw new BadApiRequestException("File with this extension "+ extension + "not allowed");
        }

        return null;
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException { // to get uploaded image from path
        String fullPath = path + File.separator + name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
