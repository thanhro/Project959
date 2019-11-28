package com.thanhld.server959.constraints;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;
import java.util.List;

public class GoogleDriveConstraints {
    public static final String APPLICATION_NAME = "Google Drive API Configuration App";

    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Directory to store user credentials for this application.
    public static final java.io.File CREDENTIALS_FOLDER //
            = new java.io.File(System.getProperty("user.home"), "credentials");

    public static final String CLIENT_SECRET_FILE_NAME = "credentials.json";

    //
    // Global instance of the scopes required by this quickstart. If modifying these
    // scopes, delete your previously saved credentials/ folder.
    //
    public static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    public static final String CALLBACK_URI = "http://localhost:8080/auth/oauth2/callback";

    public static final String USER_GOOGLE_DRIVE = "Thanhro";
}
