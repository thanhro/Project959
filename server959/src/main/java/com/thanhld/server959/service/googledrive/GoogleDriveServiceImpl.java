package com.thanhld.server959.service.googledrive;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.User;
import com.thanhld.server959.constraints.GoogleDriveConstraints;
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {

    @Autowired
    GoogleDriveServiceUtils googleDriveServiceUtils;

    public List<File> getAllFiles() {
        try {
            Drive drive = googleDriveServiceUtils.getService();
            FileList fileList = drive.files().list()
                    .setFields("nextPageToken, files(*)")
                    .execute();
            List<File> files = fileList.getFiles();
            if (files == null || files.isEmpty()) {
                return null;
            }
            return files;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
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
    public String createFolder(String folderName) throws GeneralSecurityException, IOException {
        Drive drive = googleDriveServiceUtils.getService();
        File fileMetaData = new File();
        fileMetaData.setName(folderName);
        fileMetaData.setMimeType("application/vnd.google-apps.folder");

        File file = null;
        try {
            file = drive.files().create(fileMetaData).setFields("*").execute();
            changeOwnerPermissionToCurrentUser(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return file.getWebViewLink();
    }

    @Override
    public Set<String> getAllOwnersSharedFileToTeacher() throws IOException, GeneralSecurityException {
        Drive drive = googleDriveServiceUtils.getService();
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
    public File getFolderByWebViewLink(String webViewLink) throws IOException, GeneralSecurityException {
        Drive drive = googleDriveServiceUtils.getService();
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

    public void changeOwnerPermissionToCurrentUser(File file) throws GeneralSecurityException, IOException {
        Drive drive = googleDriveServiceUtils.getService();
        JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                // Handle error
                System.err.println(e.getMessage());
            }
            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                System.out.println("Permission ID: " + permission.getId());
            }
        };
        String currentUserEmail = SecurityUtils.getCurrentUserLogin().get().getEmail();
        BatchRequest batch = drive.batch();
        Permission userPermission = new Permission()
                .setType("user")
                .setRole("owner")
                .setEmailAddress(currentUserEmail);
        drive.permissions().create(file.getId(), userPermission)
                .setFields("*").setTransferOwnership(true)
                .queue(batch, callback);
        batch.execute();
        drive.permissions().delete(file.getId(), GoogleDriveConstraints.SERVICE_ACCOUNT_PERMISSION_ID).execute();
    }

}
