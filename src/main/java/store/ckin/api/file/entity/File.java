package store.ckin.api.file.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.adit.Auditable;
import store.ckin.api.book.entity.Book;
import store.ckin.api.review.entity.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * File 업로드를 위한 엔티티 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 28.
 */
@Entity
@Table(name = "File")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class File extends Auditable {

    @Id
    @Column(name = "file_id")
    private String fileId;

    @Column(name = "file_origin_name")
    private String fileOriginName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_extension")
    private String fileExtension;

    @NotNull
    @Column(name = "file_category")
    private String fileCategory;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

}
