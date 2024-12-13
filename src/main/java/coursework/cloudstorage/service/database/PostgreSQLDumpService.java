package coursework.cloudstorage.service.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.stereotype.Service;

@Service
public class PostgreSQLDumpService {

    public void createDatabaseDump(String dbName, String dbUser, String password, String outputFilePath) throws IOException {
        String command = String.format("pg_dump -U %s -d %s -f \"%s\"", dbUser, dbName, outputFilePath);

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.environment().put("PGPASSWORD", password);

        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
        }
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Ошибка при создании дампа базы данных. Код завершения: " + exitCode);
            }
            System.out.println("Дамп успешно создан");
        } catch (InterruptedException e) {
            throw new IOException("Процесс создания дампа был прерван", e);
        }
    }
}