package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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

    public void saveFile(File file, Model model) {
        model.addAttribute("success", true);
        fileMapper.saveFile(file);
    }
    public List<File> getFiles(Integer userId) {
        return fileMapper.getFiles(userId);
    }
    public void deleteFileById(Integer fileId, Integer userId) {
        fileMapper.deleteFileById(fileId, userId);
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

    public boolean isFileEmpty(MultipartFile fileData, Model model) {
        if (fileData.isEmpty()) {
            model.addAttribute("fileIsEmpty", "You can't submit the form without uploading a file.");
            model.addAttribute("error", false);
            model.addAttribute("success", false);
            return true;
        }
        return false;
    }

    public boolean isFileAlreadyUploaded(File file, Model model) {
        if(fileExists(file.getFilename(), file.getUserId())) {
            model.addAttribute("fileExists", "You cannot upload this file because it has already been uploaded.");
            model.addAttribute("success", false);
            model.addAttribute("error", false);
            return true;
        }
        return false;
    }

    public File getFileByName(String fileName, Integer userId) {
        return fileMapper.getFileByName(fileName, userId);
    }

    public void setResponseHeaders(HttpServletResponse response, File file) {
        response.setContentType(file.getContentType());
        response.setHeader("Content-Disposition", "filename=" + file.getFilename());
        response.setContentLengthLong(file.getFileSize());
    }

    public void writeToFileOutputStream(HttpServletResponse response, File file) throws IOException {
        try (OutputStream ops = response.getOutputStream()) {
            ops.write(file.getFileData(), 0, file.getFileData().length);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
