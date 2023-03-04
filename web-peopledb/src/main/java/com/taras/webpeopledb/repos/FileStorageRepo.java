package com.taras.webpeopledb.repos;

import com.taras.webpeopledb.exceptions.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileStorageRepo {
  @Value("${STORAGE_FOLDER}")
  private String storageFolder;

  public void save(String originalFilename, InputStream inputStream) {
    try {
      Path filePath = Path.of(storageFolder).resolve(originalFilename).normalize();
      Files.copy(inputStream, filePath);
    } catch (IOException e) {
      throw new StorageException(e);
    }
  }

  public Resource findByName(String fileName) {
    try {
      Path filePath = Path.of(storageFolder).resolve(fileName).normalize();
      return new UrlResource(filePath.toUri());
    } catch (MalformedURLException e) {
      throw new StorageException(e);
    }
  }

  public void deleteAllByName(Collection<String> fileNames) {
    try {
      for (String fileName : fileNames.stream().filter(Objects::nonNull).filter(f -> !f.equals("")).collect(Collectors.toSet())) {
        Path filePath = Path.of(storageFolder).resolve(fileName).normalize();
        Files.deleteIfExists(filePath);
      }
    } catch (IOException e) {
      throw new StorageException(e);
    }
  }
}
