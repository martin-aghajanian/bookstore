package com.martin.bookstore.service;

import com.martin.bookstore.core.enums.CoverImageFileName;
import com.martin.bookstore.core.enums.CoverImageSize;
import com.martin.bookstore.core.enums.FileDownloadStatus;
import com.martin.bookstore.core.enums.FileType;
import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.BookFileInfo;
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
        Map<String, FileInfo> existingByUrl = new HashMap<>();
        for (FileInfo fi : fileInfoRepository.findAll()) {
            if (fi.getFileUrl() != null) {
                existingByUrl.put(fi.getFileUrl() + "|" + fi.getFileName(), fi);
            }
        }

        Queue<FileInfo> toSave = new ConcurrentLinkedQueue<>();
        ExecutorService exec = Executors.newFixedThreadPool(8);
        CountDownLatch latch = new CountDownLatch(books.size());

        for (Book book : books) {
            exec.submit(() -> {
                try {
                    processSingleBook(book, existingByUrl, toSave);
                } catch (Exception ignored) {
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
        exec.shutdown();

        if (!toSave.isEmpty()) {
            fileInfoRepository.saveAll(toSave);
        }
    }

    private void processSingleBook(Book book,
                                   Map<String, FileInfo> existingByUrl,
                                   Queue<FileInfo> toSave) {
        String url = book.getCoverImageUrl();
        boolean accessible = isImageUrlValid(url);
        Path baseDir = Paths.get(rootDir, String.valueOf(book.getId()));
        Path thumbDir = baseDir.resolve("thumbnails");

        try {
            if (accessible) {
                Files.createDirectories(thumbDir);
                Path fullPath = baseDir.resolve(CoverImageFileName.FULL.getFileName());
                downloadImage(url, fullPath);
                for (CoverImageFileName v : Arrays.asList(
                        CoverImageFileName.SMALL,
                        CoverImageFileName.MEDIUM,
                        CoverImageFileName.LARGE)) {
                    Path thumbPath = thumbDir.resolve(v.getFileName());
                    CoverImageSize sz = CoverImageSize.valueOf(v.name());
                    Thumbnails.of(fullPath.toFile())
                            .size(sz.getWidth(), sz.getHeight())
                            .toFile(thumbPath.toFile());
                }
                for (CoverImageFileName v : CoverImageFileName.values()) {
                    boolean isFull = v == CoverImageFileName.FULL;
                    String fn = v.getFileName();
                    String fu = isFull ? url : null;
                    Path fp = isFull ? baseDir.resolve(fn) : thumbDir.resolve(fn);
                    FileInfo fi = existingByUrl.getOrDefault(url + "|" + fn, new FileInfo());
                    fi.setFileUrl(fu);
                    fi.setFileName(fn);
                    fi.setFilePath(fp.toString());
                    fi.setFileFormat(FileType.JPG.getType());
                    fi.setStatus(FileDownloadStatus.COMPLETED);
                    fi.setErrorMessage(null);

                    BookFileInfo bf = new BookFileInfo();
                    bf.setBook(book);
                    bf.setFileInfo(fi);
                    fi.getBookFileInfos().add(bf);

                    toSave.add(fi);
                }
            } else {
                for (CoverImageFileName v : CoverImageFileName.values()) {
                    boolean isFull = v == CoverImageFileName.FULL;
                    FileInfo fi = new FileInfo();
                    fi.setFileUrl(isFull ? url : null);
                    fi.setFileName(v.getFileName());
                    fi.setFilePath(null);
                    fi.setFileFormat(FileType.UNKNOWN.getType());
                    fi.setStatus(FileDownloadStatus.FAILED);
                    fi.setErrorMessage("URL not accessible");

                    BookFileInfo bf = new BookFileInfo();
                    bf.setBook(book);
                    bf.setFileInfo(fi);
                    fi.getBookFileInfos().add(bf);

                    toSave.add(fi);
                }
            }
        } catch (Exception e) {
            // handle exceptions if needed
        }
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
}
