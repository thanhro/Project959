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
import com.thanhld.server959.web.rest.errors.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {

    @Autowired
    GoogleDriveServiceUtils googleDriveServiceUtils;

    @Override
    public Drive getService() {
        try {
            return googleDriveServiceUtils.getService();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteFileByLink(String link) {
        List<File> files = getAllFiles();
        if (files == null)
            return;
        for (File file : files) {
            if (file.getWebViewLink().equals(link)) {
                Drive googleDrive = null;
                try {
                    googleDrive = googleDriveServiceUtils.getService();
                    if (googleDrive != null) {
                        googleDrive.files().delete(file.getId()).execute();
                    }
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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

    private String getParentFileIdFromWebViewLink(FileList fileList, String parentFileWebViewLink) throws IOException {
        String parentFileId = "";
        for (File file : fileList.getFiles()) {
            if (file.getWebViewLink().equals(parentFileWebViewLink)) {
                parentFileId = file.getId();
            }
        }
        return parentFileId;
    }

    private FileList getFileList() throws GeneralSecurityException, IOException {
        Drive drive = googleDriveServiceUtils.getService();

        FileList fileList = drive.files().list().setFields("*").execute();//setQ("mimeType = 'application/vnd.google-apps.document'")
        if (fileList == null)
            return null;

        return fileList;
    }


    //
    @Override
    public Set<String> getAllDisplayNameInParentFile(String parentFileWebViewLink) throws IOException, GeneralSecurityException {
        FileList fileList = getFileList();
        String parentFileId = getParentFileIdFromWebViewLink(fileList, parentFileWebViewLink);
        Set<String> studentNames = new HashSet<>();
        for (File file : fileList.getFiles()) {
            List<String> parents = file.getParents();
            if (parents != null) {
                for (String parent : parents) {
                    if (parent.equals(parentFileId)) {
                        List<User> users = file.getOwners();
                        studentNames.add(users.get(0).getDisplayName());
                    }
                }
            }
        }

        return studentNames;
    }


    @Override
    public Set<String> getAllWebViewLinkInParentFile(String parentFileWebViewLink) throws GeneralSecurityException, IOException {
        FileList fileList = getFileList();
        String parentFileId = getParentFileIdFromWebViewLink(fileList, parentFileWebViewLink);
        Set<String> studentWebViewLinks = new HashSet<>();
        for (File file : fileList.getFiles()) {
            List<String> parents = file.getParents();
            if (parents != null) {
                for (String parent : parents) {
                    if (parent.equals(parentFileId)) {
                        studentWebViewLinks.add(file.getWebViewLink());
                    }
                }
            }
        }

        return studentWebViewLinks;
    }

    @Override
    public Map<String, String> getAllDisplayNameAndWebViewLinkInParentFile(String parentFileWebViewLink) throws GeneralSecurityException, IOException {
        FileList fileList = getFileList();
        String parentFileId = getParentFileIdFromWebViewLink(fileList, parentFileWebViewLink);
        Map<String, String> studentDetails = new HashMap<>();
        for (File file : fileList.getFiles()) {
            List<String> parents = file.getParents();
            if (parents != null) {
                for (String parent : parents) {
                    if (parent.equals(parentFileId)) {
                        if (parent.equals(parentFileId)) {
                            List<User> users = file.getOwners();
                            studentDetails.put(users.get(0).getDisplayName(), file.getWebViewLink());
                        }
                    }
                }
            }
        }
        return studentDetails;
    }

    @Override
    public Map<String, String> getDisplayNameAndWebViewLinkInParentFile(String currentUserName, String parentFileWebViewLink) throws GeneralSecurityException, IOException {
        Map<String,String> allUser = getAllDisplayNameAndWebViewLinkInParentFile(parentFileWebViewLink);
        if (!allUser.containsKey(currentUserName))
            return null;
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put(currentUserName,allUser.get(currentUserName));
        return userDetails;
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
        for (File file : fileList.getFiles()) {
            if (file.getWebViewLink().equals(webViewLink))
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
                .setRole("writer")
                .setEmailAddress(currentUserEmail);
        Permission anyonePermission = new Permission()
                .setType("anyone")
                .setRole("writer");

        drive.permissions().create(file.getId(), userPermission)
                .setFields("*")
                .queue(batch, callback);
        drive.permissions().create(file.getId(), anyonePermission)
                .setFields("*")
                .queue(batch, callback);

        batch.execute();
//        drive.permissions().delete(file.getId(), GoogleDriveConstraints.SERVICE_ACCOUNT_PERMISSION_ID).execute();
    }

    @Override
    public void updateFileNameByLink(String link, String targetName) {
        Drive drive = null;
        try {
            drive = googleDriveServiceUtils.getService();
            FileList fileList = drive.files().list()
                    .setFields("files(id,name,webViewLink)")
                    .execute();
            if (fileList == null)
                return;
            for (File file : fileList.getFiles()) {
                if (file.getWebViewLink().equals(link)) {
                    File newContent = new File();
                    newContent.setName(targetName);
                    drive.files().update(file.getId(), newContent).execute();
                }
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
