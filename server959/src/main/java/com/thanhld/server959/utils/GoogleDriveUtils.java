package com.thanhld.server959.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.thanhld.server959.constraints.GoogleDriveConstraints;

import java.io.*;
import java.security.GeneralSecurityException;

public class GoogleDriveUtils {

    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        java.io.File clientSecretFilePath = new java.io.File(GoogleDriveConstraints.CREDENTIALS_FOLDER, GoogleDriveConstraints.CLIENT_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + GoogleDriveConstraints.CLIENT_SECRET_FILE_NAME //
                    + " to folder: " + GoogleDriveConstraints.CREDENTIALS_FOLDER.getAbsolutePath());
        }

        InputStream in = new FileInputStream(clientSecretFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GoogleDriveConstraints.JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, GoogleDriveConstraints.JSON_FACTORY,
                clientSecrets, GoogleDriveConstraints.SCOPES).setDataStoreFactory(new FileDataStoreFactory(GoogleDriveConstraints.CREDENTIALS_FOLDER))
                .setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(8888).build()).authorize("user");
    }

    public static Drive getService() {
        try {
            NetHttpTransport HTTP_TRANSPORT;
            Credential credential;
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            credential = getCredentials(HTTP_TRANSPORT);
            return new Drive.Builder(HTTP_TRANSPORT, GoogleDriveConstraints.JSON_FACTORY, credential) //
                    .setApplicationName(GoogleDriveConstraints.APPLICATION_NAME).build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
