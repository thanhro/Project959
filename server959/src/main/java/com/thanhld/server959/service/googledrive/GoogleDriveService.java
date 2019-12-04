package com.thanhld.server959.service.googledrive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GoogleDriveService {
    Drive getService();

    void deleteFileByLink(String link);

    List<File> getAllFiles();

    List<String> getAllFileNames();

    List<String> getAllFileIds();

    List<String> getAllFileOwners() throws IOException;

    String createFolder(String assignmentName) throws GeneralSecurityException, IOException;

    Set<String> getAllDisplayNameInParentFile(String parentFileWebViewLink) throws Exception;

    File getFolderByWebViewLink(String webViewLink) throws Exception;

    void changeOwnerPermissionToCurrentUser(File file) throws GeneralSecurityException, IOException;

    void updateFileNameByLink(String link, String targetName);

    Set<String> getAllWebViewLinkInParentFile(String parentFileWebViewLink) throws GeneralSecurityException, IOException;

    Map<String, String> getAllDisplayNameAndWebViewLinkInParentFile(String parentFileWebViewLink) throws GeneralSecurityException, IOException;

    Map<String, String> getDisplayNameAndWebViewLinkInParentFile(String currentUserEmail, String parentFileWebViewLink) throws GeneralSecurityException, IOException;

}
