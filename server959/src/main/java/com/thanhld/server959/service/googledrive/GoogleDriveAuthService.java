package com.thanhld.server959.service.googledrive;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.services.drive.Drive;

public interface GoogleDriveAuthService {
    GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow();

    Drive getService();

    void saveToken(String code) throws Exception;
}
