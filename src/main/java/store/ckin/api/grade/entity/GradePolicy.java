package store.ckin.api.grade.entity;

import lombok.Getter;

/**
 * 등급 ID와 이름에 연관된 정책을 Enum 으로 구성한 클래스 입니다.
 * 1** : 일반
 * 2** : 로얄
 * ...
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
@Getter
public enum GradePolicy {
    NORMAL(1, "노말"),
    ROYAL(2, "로얄"),
    GOLD(3, "골드"),
    PLATINUM(4, "플래티넘");

    private final int code;

    private final String name;

    GradePolicy(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 연관 번호로 등급 이름을 조회하는 메서드 입니다.
     */
    public static String getNameByCode(int code) {
        for (GradePolicy grade : GradePolicy.values()) {
            if (grade.getCode() == code) {
                return grade.getName();
            }
        }
        return null;
    }

    /**
     * 등급 이름으로 연관번호를 조회하는 메서드 입니다.
     */
    public static int getCodeByName(String name) {
        for (GradePolicy grade : GradePolicy.values()) {
            if (grade.getName().equals(name)) {
                return grade.getCode();
            }
        }
        return -1;
    }
}
