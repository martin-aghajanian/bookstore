// File: com/martin/bookstore/service/CoverImageService.java
package com.martin.bookstore.fileInfo.coverImg.service;

import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.entity.BookFileInfo;
import com.martin.bookstore.book.persistence.repository.BookRepository;
import com.martin.bookstore.fileInfo.enums.FileDownloadStatus;
import com.martin.bookstore.fileInfo.persistence.entity.FileInfo;
import com.martin.bookstore.fileInfo.persistence.repository.FileInfoRepository;
import com.martin.bookstore.fileInfo.enums.FileType;
import com.martin.bookstore.fileInfo.coverImg.enums.CoverImageFileName;
import com.martin.bookstore.fileInfo.coverImg.enums.CoverImageSize;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CoverImageService {
    private final BookRepository bookRepository;
    private final FileInfoRepository fileInfoRepository;

    @Value("${bookstore.image.cover.root.dir}")
    private String rootDir;

    @Transactional
    public void processBookCovers() {
        List<Book> books = bookRepository.findAll(Pageable.ofSize(10)).getContent();
        ExecutorService exec = Executors.newFixedThreadPool(8);

        try {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (Book book : books) {
                futures.add(CompletableFuture.runAsync(() -> processSingleBook(book), exec));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } finally {
            exec.shutdown();
        }
    }

    private void processSingleBook(Book book) {
        String url = book.getCoverImageUrl();
        Path baseDir = Paths.get(rootDir, book.getId().toString());
        Map<CoverImageFileName, FileInfo> fiMap = new EnumMap<>(CoverImageFileName.class);

        try {
            Files.createDirectories(baseDir);

            for (CoverImageFileName sizeKey : List.of(
                    CoverImageFileName.FULL,
                    CoverImageFileName.MEDIUM,
                    CoverImageFileName.SMALL
            )) {
                FileInfo fi = fileInfoRepository
                        .findByBookAndFileName(book, sizeKey.getFileName())
                        .orElseGet(FileInfo::new);

                List<BookFileInfo> bfList = Optional.ofNullable(fi.getBookFileInfos())
                        .orElse(new ArrayList<>());

                boolean alreadyLinked = bfList.stream()
                        .anyMatch(bf -> bf.getBook().getId().equals(book.getId()));
                if (!alreadyLinked) {
                    BookFileInfo bf = new BookFileInfo();
                    bf.setBook(book);
                    bf.setFileInfo(fi);
                    bfList.add(bf);
                }
                fi.setBookFileInfos(bfList);

                fi.setFileUrl(url);
                fi.setFileName(sizeKey.getFileName());
                fi.setFileFormat(FileType.UNKNOWN.getType());
                fi.setStatus(FileDownloadStatus.valueOf(FileDownloadStatus.PENDING.name()));

                fileInfoRepository.save(fi);
                fiMap.put(sizeKey, fi);
            }

            FileInfo fullFi = fiMap.get(CoverImageFileName.FULL);
            fullFi.setStatus(FileDownloadStatus.valueOf(FileDownloadStatus.DOWNLOADING.name()));
            fileInfoRepository.save(fullFi);

            Path fullPath = baseDir.resolve(CoverImageFileName.FULL.getFileName());
            downloadImage(url, fullPath);

            fullFi.setFilePath(fullPath.toString());
            fullFi.setFileFormat(FileType.JPG.getType());
            fullFi.setStatus(FileDownloadStatus.valueOf(FileDownloadStatus.COMPLETED.name()));
            fileInfoRepository.save(fullFi);

            for (CoverImageFileName sizeKey : List.of(
                    CoverImageFileName.MEDIUM,
                    CoverImageFileName.SMALL
            )) {
                FileInfo thumbFi = fiMap.get(sizeKey);
                thumbFi.setStatus(FileDownloadStatus.valueOf(FileDownloadStatus.DOWNLOADING.name()));
                fileInfoRepository.save(thumbFi);

                Path thumbPath = baseDir.resolve(sizeKey.getFileName());
                var sz = CoverImageSize.valueOf(sizeKey.name());
                Thumbnails.of(fullPath.toFile())
                        .size(sz.getWidth(), sz.getHeight())
                        .toFile(thumbPath.toFile());

                thumbFi.setFilePath(thumbPath.toString());
                thumbFi.setFileFormat(FileType.JPG.getType());
                thumbFi.setStatus(FileDownloadStatus.valueOf(FileDownloadStatus.COMPLETED.name()));
                fileInfoRepository.save(thumbFi);
            }

        } catch (IOException e) {
            fiMap.values().forEach(fi -> {
                fi.setStatus(FileDownloadStatus.valueOf(FileDownloadStatus.FAILED.name()));
                fi.setErrorMessage(e.getMessage());
                fileInfoRepository.save(fi);
            });
        }
    }

    private void downloadImage(String urlStr, Path outputPath) throws IOException {
        try (InputStream in = new URL(urlStr).openStream()) {
            Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
