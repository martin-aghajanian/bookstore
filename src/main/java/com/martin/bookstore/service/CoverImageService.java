package com.martin.bookstore.service;

import com.martin.bookstore.core.enums.CoverImageFileName;
import com.martin.bookstore.core.enums.CoverImageSize;
import com.martin.bookstore.core.enums.FileType;
import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.FileInfo;
import com.martin.bookstore.repository.BookRepository;
import com.martin.bookstore.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CoverImageService {
    private final BookRepository bookRepository;
    private final FileInfoRepository fileInfoRepository;

    @Value("${bookstore.image.cover.root.dir}")
    private String rootDir;

    public void processBookCovers() {
        List<Book> books = bookRepository.findAll(Pageable.ofSize(10)).getContent();
        Map<String, FileInfo> urlToFileInfo = new HashMap<>();
        for (FileInfo fi : fileInfoRepository.findAll()) {
            urlToFileInfo.put(fi.getUrl(), fi);
        }


        Queue<FileInfo> fileInfosToSave = new ConcurrentLinkedQueue<>();
        int total = books.size();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CountDownLatch latch = new CountDownLatch(total);

        for (Book book : books) {
            executor.submit(() -> {
                try {
                    processSingleBook(book, urlToFileInfo, fileInfosToSave);
                } catch (Exception e) {
                    System.err.println("error processing book ID " + book.getId() + ": " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();

        if (!fileInfosToSave.isEmpty()) {
            fileInfoRepository.saveAll(fileInfosToSave);
        }
    }

    private void processSingleBook(Book book, Map<String, FileInfo> urlToFileInfo, Queue<FileInfo> fileInfosToSave) {
        String imageUrl = book.getCoverImageUrl();
        Long id = book.getId();

        FileInfo existing = urlToFileInfo.get(imageUrl);
        if (existing != null && Boolean.TRUE.equals(existing.getIsAccessible())) return;

        try {
            boolean isAccessible = isImageUrlValid(imageUrl);
            Path bookDir = Paths.get(rootDir, String.valueOf(id));
            Path thumbDir = bookDir.resolve("thumbnails");

            if (isAccessible) {
                Files.createDirectories(thumbDir);
                Path fullImagePath = bookDir.resolve(CoverImageFileName.FULL.getFileName());
                downloadImage(imageUrl, fullImagePath);

                createThumbnail(fullImagePath, thumbDir.resolve(
                        CoverImageFileName.SMALL.getFileName()),
                        CoverImageSize.SMALL.getWidth(),
                        CoverImageSize.SMALL.getHeight()
                );
//                createThumbnail(fullImagePath, thumbDir.resolve("medium.jpg"), 200, 300);
//                createThumbnail(fullImagePath, thumbDir.resolve("large.jpg"), 400, 600);

                if (existing != null) {
                    existing.setIsAccessible(true);
                    existing.setFileType(FileType.JPG.getType());
                    existing.setLocalPath(fullImagePath.toString());
                    fileInfosToSave.add(existing);
                } else {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setBook(book);
                    fileInfo.setUrl(imageUrl);
                    fileInfo.setIsAccessible(true);
                    fileInfo.setFileType(FileType.JPG.getType());
                    fileInfo.setLocalPath(fullImagePath.toString());
                    fileInfosToSave.add(fileInfo);
                }

            } else {
                if (existing != null) {
                    existing.setIsAccessible(false);
                    existing.setFileType(FileType.UNKNOWN.getType());
                    existing.setLocalPath(null);
                    fileInfosToSave.add(existing);
                } else {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setBook(book);
                    fileInfo.setUrl(imageUrl);
                    fileInfo.setIsAccessible(false);
                    fileInfo.setFileType(FileType.UNKNOWN.getType());
                    fileInfo.setLocalPath(null);
                    fileInfosToSave.add(fileInfo);
                }
            }

        } catch (Exception e) {
            FileInfo fileInfo = urlToFileInfo.get(imageUrl);
            if (fileInfo != null) {
                fileInfo.setIsAccessible(false);
                fileInfo.setFileType(FileType.UNKNOWN.getType());
                fileInfo.setLocalPath(null);
                fileInfosToSave.add(fileInfo);
            } else {
                FileInfo newFileInfo = new FileInfo();
                newFileInfo.setBook(book);
                newFileInfo.setUrl(imageUrl);
                newFileInfo.setIsAccessible(false);
                newFileInfo.setFileType(FileType.UNKNOWN.getType());
                newFileInfo.setLocalPath(null);
                fileInfosToSave.add(newFileInfo);
            }
        }

        System.out.println("book ID "+id+" image processed");
    }

    private boolean isImageUrlValid(String urlStr) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setRequestMethod("HEAD");
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private void downloadImage(String urlStr, Path outputPath) throws IOException {
        try (InputStream inputStream = new URL(urlStr).openStream()) {
            Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void createThumbnail(Path sourcePath, Path outputPath, int width, int height) throws IOException {
        Thumbnails.of(sourcePath.toFile()).size(width, height).toFile(outputPath.toFile());
    }
}
