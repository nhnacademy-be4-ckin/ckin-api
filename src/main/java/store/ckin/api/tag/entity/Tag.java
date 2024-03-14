package store.ckin.api.tag.entity;

import lombok.*;

import javax.persistence.*;

/**
 * description:
 *
 * @author : 김준현
 * @version : 2024. 02. 13
 */
@Entity
@Table(name = "Tag")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_name")
    private String tagName;

    public void updateTagName(String tagName) {
        this.tagName = tagName;
    }
}
