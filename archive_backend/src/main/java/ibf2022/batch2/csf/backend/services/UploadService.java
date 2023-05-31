package ibf2022.batch2.csf.backend.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;

import ibf2022.batch2.csf.backend.exceptions.MongoDatabaseException;
import ibf2022.batch2.csf.backend.models.Archive;
import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.repositories.ImageRepository;

@Service
public class UploadService {

    private static final String TEMP = "temp";

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    public void recordBundle(Archive a) throws MongoDatabaseException {
      archiveRepository.recordBundle(a);  
    }

    public Optional<Archive> getBundleByBundleId(String bundleId) {
      return archiveRepository.getBundleByBundleId(bundleId);
    }

    public List<Archive> getBundles() {
      return archiveRepository.getBundles();
    }

    public List<String> upload(String name, String title, String comments, MultipartFile archive) throws IOException, AmazonServiceException, SdkClientException {
        List<String> urls = new LinkedList<>();
        File[] unzippedFiles = unzip(archive);
        for (File file: unzippedFiles) {
          URL url = imageRepository.upload(name, title, comments, file);
          urls.add(url.toString());
        }
        deleteTempAfterUpload(Paths.get("").toAbsolutePath().resolve(TEMP));
        return urls;
    }

    private File[] unzip(MultipartFile file) throws IOException {
        ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
        Path path = Paths.get("").toAbsolutePath().resolve(TEMP);
        for (ZipEntry entry; (entry = inputStream.getNextEntry()) != null; ) {
          Path resolvedPath = path.resolve(entry.getName());
          if (!entry.isDirectory()) {
            Files.createDirectories(resolvedPath.getParent());
            Files.copy(inputStream, resolvedPath);
          } else {
            Files.createDirectories(resolvedPath);
          }
          System.out.println(path.toFile().listFiles());
        }
        return path.toFile().listFiles();
    }

    private void deleteTempAfterUpload(Path path) {
      for (File file: path.toFile().listFiles()) {
        file.delete();
      }
    }

}
