package com.thanhld.server959.service.googledrive;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.thanhld.server959.constraints.GoogleDriveConstraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class GoogleDriveServiceUtils {
    @Autowired
    private GoogleCredential googleCredential;

    @Bean
    public Drive getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT,
                JacksonFactory.getDefaultInstance(), googleCredential)
                .build();
    }

    @Bean
    public GoogleCredential googleCredential() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(GoogleDriveConstraints.SERVICE_ACCOUNT_ID)
                .setServiceAccountScopes(GoogleDriveConstraints.SCOPES)
                .setServiceAccountPrivateKeyFromP12File(ResourceUtils.getFile("classpath:" + GoogleDriveConstraints.CLIENT_SECRET_FILE_NAME))
                .build();
    }
}
