package com.thanhld.server959.service.googledrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.List;

public interface GoogleDriveService {
     Credential getCredentials(NetHttpTransport httpTransport) throws IOException;
     List<File> getAllFiles();
     List<String> getAllFileNames();
     List<String> getAllFileIds();
     List<String> getAllFileOwners() throws IOException;
}
