package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Boolean fileExists(String fileName, Integer userId) {
        return fileMapper.getFileByName(fileName, userId) != null;
    }

    public void saveFile(File file) {
        fileMapper.saveFile(file);
    }
    public List<File> getFiles(Integer userId) {
        return fileMapper.getFiles(userId);
    }
    public void deleteFileById(Integer fileId) {
        fileMapper.deleteFileById(fileId);
    }

    public File setFileInformation(MultipartFile fileData, Integer userId) throws IOException {
        File file = new File();
        file.setFilename(fileData.getOriginalFilename());
        file.setContentType(fileData.getContentType());
        file.setFileSize(fileData.getSize() ); // Convert Byte -> KB -> MB
        file.setFileData(fileData.getBytes());
        file.setUserId(userId);
        return file;
    }

}
