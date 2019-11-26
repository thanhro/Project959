package com.thanhld.server959.service.googledrive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.User;
import com.thanhld.server959.utils.GoogleDriveUtils;
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public String createFolder(String folderName) {
        Drive drive = GoogleDriveUtils.getService();
        File fileMetaData = new File();
        fileMetaData.setName(folderName);
        fileMetaData.setMimeType("application/vnd.google-apps.folder");

        File file = null;
        try {
            file = drive.files().create(fileMetaData).setFields("*").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getWebViewLink();
    }

    @Override
    public Set<String> getAllOwnersSharedFileToTeacher() throws IOException {
        Drive drive = GoogleDriveUtils.getService();
        String currentEmail = SecurityUtils.getCurrentUserLogin().get().getEmail();
        FileList fileList = drive.files().list().setFields("*").setQ("mimeType = 'application/vnd.google-apps.document'").execute();
        if (fileList == null)
            return null;
        Set<String> ownersSharedFileToTeacher = new HashSet<>();
        for (File file : fileList.getFiles()){
            List<User> owners = file.getOwners();
            for (User owner: owners){
                if(owner.getEmailAddress().equals(currentEmail))
                    continue;
                ownersSharedFileToTeacher.add(owner.getDisplayName());
            }
        }
        return ownersSharedFileToTeacher;
    }

    public List<String> getAllFileNames() {
        List<String> fileNames = new ArrayList<>();
        for (File file : getAllFiles()) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    @Override
    public File getFolderByWebViewLink(String webViewLink) throws IOException {
        Drive drive = GoogleDriveUtils.getService();
        FileList fileList = drive.files().list().setFields("*").setQ("mimeType = 'application/vnd.google-apps.folder'").execute();
        if (fileList == null)
            return null;
        File folder = null;
        for (File file : fileList.getFiles()){
            if (file.getWebViewLink().equals(webViewLink) )
                folder = file;
        }
        return folder;
    }
}
