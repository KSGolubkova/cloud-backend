package coursework.cloudstorage.service.yandex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.stereotype.Service;

@Service
public class YandexDiskUploadService {

    private static final String YANDEX_DISK_API_URL = "https://cloud-api.yandex.net/v1/disk/resources/upload";

    public void uploadFileToYandexDisk(String oauthToken, String localFilePath, String remoteFilePath) throws IOException {
        String uploadUrl = getUploadUrl(oauthToken, remoteFilePath);

        uploadFile(uploadUrl, new File(localFilePath));
    }

    private String getUploadUrl(String oauthToken, String remoteFilePath) throws IOException {
        URL url = new URL(YANDEX_DISK_API_URL + "?path=" + remoteFilePath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "OAuth " + oauthToken);
        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new IOException("Ошибка при получении URL для загрузки файла: " + connection.getResponseCode());
        }

        InputStream responseStream = connection.getInputStream();
        String response = new String(responseStream.readAllBytes());
        responseStream.close();

        String uploadUrl = response.split("\"href\":\"")[1].split("\"")[0];
        return uploadUrl;
    }

    private void uploadFile(String uploadUrl, File file) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            connection.getOutputStream().write(fileInputStream.readAllBytes());
        }

        if (connection.getResponseCode() != 201) {
            throw new IOException("Ошибка при загрузке файла на Яндекс.Диск: " + connection.getResponseCode());
        }
    }
}