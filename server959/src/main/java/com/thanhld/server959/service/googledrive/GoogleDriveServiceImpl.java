package com.thanhld.server959.service.googledrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.PermissionList;
import com.thanhld.server959.constraints.GoogleDriveConstraints;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {

    final Drive drive = getService();

    public Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        java.io.File clientSecretFilePath = new java.io.File(GoogleDriveConstraints.CREDENTIALS_FOLDER, GoogleDriveConstraints.CLIENT_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + GoogleDriveConstraints.CLIENT_SECRET_FILE_NAME //
                    + " to folder: " + GoogleDriveConstraints.CREDENTIALS_FOLDER.getAbsolutePath());
        }

        // Load client secrets.
        InputStream in = new FileInputStream(clientSecretFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GoogleDriveConstraints.JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, GoogleDriveConstraints.JSON_FACTORY,
                clientSecrets, GoogleDriveConstraints.SCOPES).setDataStoreFactory(new FileDataStoreFactory(GoogleDriveConstraints.CREDENTIALS_FOLDER))
                .setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(8888).build()).authorize("user");
    }

    public Drive getService() {
        try {
            NetHttpTransport HTTP_TRANSPORT;
            Credential credential;
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            credential = getCredentials(HTTP_TRANSPORT);

            // 5: Create Google Drive Service.
            return new Drive.Builder(HTTP_TRANSPORT, GoogleDriveConstraints.JSON_FACTORY, credential) //
                    .setApplicationName(GoogleDriveConstraints.APPLICATION_NAME).build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<File> getAllFiles() {
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

    public List<String> getAllFileNames() {
        List<String> fileNames = new ArrayList<>();
        for (File file : getAllFiles()) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }
}
