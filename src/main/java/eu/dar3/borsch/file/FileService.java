package eu.dar3.borsch.file;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    // Ścieżka, gdzie pliki będą zapisywane na serwerze
    private static final String UPLOAD_DIR = "uploads/";

    public String saveFileAndGetUrl(MultipartFile file) throws IOException {
        // Upewnij się, że katalog do zapisywania plików istnieje
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Wygeneruj unikalną nazwę pliku, aby uniknąć nadpisywania
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

        // Zapisz plik na dysku
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        // Zwróć URL do pliku (można to dostosować do własnych potrzeb)
        return "/uploads/" + uniqueFileName;
    }

    // Metoda pomocnicza do uzyskania rozszerzenia pliku
    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex >= 0) {
            return fileName.substring(lastIndex + 1);
        }
        return "";
    }

}
