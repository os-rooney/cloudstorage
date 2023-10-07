package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.stereotype.Service;

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
}
