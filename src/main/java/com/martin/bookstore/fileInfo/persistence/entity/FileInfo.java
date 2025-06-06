package com.martin.bookstore.fileInfo.persistence.entity;

import com.martin.bookstore.book.persistence.entity.BookFileInfo;
import com.martin.bookstore.fileInfo.enums.FileDownloadStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "file_info",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_file_url_file_name",
                        columnNames = { "file_url", "file_name" }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_info_id_seq")
    @SequenceGenerator(name = "file_info_id_seq", sequenceName = "file_info_id_seq")
    private Long id;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FileDownloadStatus status;

    @Column(name = "file_format")
    private String fileFormat;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "fileInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookFileInfo> bookFileInfos = new ArrayList<>();
}
