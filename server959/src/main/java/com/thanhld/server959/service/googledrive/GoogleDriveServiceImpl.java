package com.thanhld.server959.service.googledrive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.thanhld.server959.utils.GoogleDriveUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {

    public List<File> getAllFiles() {
        Drive drive = GoogleDriveUtils.getService();
        try {
            FileList fileList = drive.files().list()
                    .setFields("nextPageToken, files(name)")
                    .execute();
            List<File> files = fileList.getFiles();
            if (files == null || files.isEmpty()) {
                return null;
            }
            return files;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllFileIds() {
        List<String> fileNames = new ArrayList<>();
        for (File file : getAllFiles()) {
            fileNames.add(file.getId());
        }
        return fileNames;
    }

    @Override
    public List<String> getAllFileOwners() throws IOException {
        return null;
    }

    @Override
    public String createFolderClassAssignment(String assignmentName) {
        Drive drive = GoogleDriveUtils.getService();
        File fileMetaData = new File();
        fileMetaData.setName(assignmentName);
        fileMetaData.setMimeType("application/vnd.google-apps.folder");

        File file = null;
        try {
            file = drive.files().create(fileMetaData).setFields("*").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getWebViewLink();
    }

    public List<String> getAllFileNames() {
        List<String> fileNames = new ArrayList<>();
        for (File file : getAllFiles()) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }
}
