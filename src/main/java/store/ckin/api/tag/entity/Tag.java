package store.ckin.api.tag.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description:
 *
 * @author : S20184366
 * @version : 2024. 02. 13
 */
@Entity
@Table(name = "Tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

}
