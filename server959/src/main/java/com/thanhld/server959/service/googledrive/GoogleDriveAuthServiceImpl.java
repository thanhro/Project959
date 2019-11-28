package com.thanhld.server959.service.googledrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.thanhld.server959.constraints.GoogleDriveConstraints;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.GeneralSecurityException;

@Service
public class GoogleDriveAuthServiceImpl implements GoogleDriveAuthService {

    private GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;

    @PostConstruct
    public void googleAuth() throws IOException, GeneralSecurityException {
        java.io.File clientSecretFilePath = new java.io.File(GoogleDriveConstraints.CREDENTIALS_FOLDER, GoogleDriveConstraints.CLIENT_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + GoogleDriveConstraints.CLIENT_SECRET_FILE_NAME //
                    + " to folder: " + GoogleDriveConstraints.CREDENTIALS_FOLDER.getAbsolutePath());
        }

        InputStream in = new FileInputStream(clientSecretFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GoogleDriveConstraints.JSON_FACTORY, new InputStreamReader(in));

        googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), GoogleDriveConstraints.JSON_FACTORY,
                clientSecrets, GoogleDriveConstraints.SCOPES).setDataStoreFactory(new FileDataStoreFactory(GoogleDriveConstraints.CREDENTIALS_FOLDER))
                .setAccessType("offline").build();
    }

    @Override
    public GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow() {
        return googleAuthorizationCodeFlow;
    }

    @Override
    public Drive getService() {
        try {
            NetHttpTransport HTTP_TRANSPORT;
            Credential credential;
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            credential = googleAuthorizationCodeFlow.loadCredential(GoogleDriveConstraints.USER_GOOGLE_DRIVE);
            return new Drive.Builder(HTTP_TRANSPORT, GoogleDriveConstraints.JSON_FACTORY, credential)
                    .setApplicationName(GoogleDriveConstraints.APPLICATION_NAME).build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveToken(String code) throws Exception {
        GoogleTokenResponse response = googleAuthorizationCodeFlow.newTokenRequest(code).setRedirectUri(GoogleDriveConstraints.CALLBACK_URI).execute();
        googleAuthorizationCodeFlow.createAndStoreCredential(response, GoogleDriveConstraints.USER_GOOGLE_DRIVE);
    }
}
