package com.thanhld.server959.service.googledrive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;

public interface GoogleDriveService {
    Drive getService();

    List<File> getAllFiles();

    List<String> getAllFileNames();

    List<String> getAllFileIds();

    List<String> getAllFileOwners() throws IOException;

    String createFolder(String assignmentName) throws GeneralSecurityException, IOException;

    Set<String> getAllOwnersSharedFileToTeacher() throws Exception;

    File getFolderByWebViewLink(String webViewLink) throws Exception;

    void changeOwnerPermissionToCurrentUser(File file) throws GeneralSecurityException, IOException;
}
