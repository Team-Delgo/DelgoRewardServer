package com.delgo.reward.domain.certification;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.record.certification.CertCreate;
import com.delgo.reward.record.certification.CertUpdate;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;
import java.util.List;
import io.hypersistence.utils.hibernate.type.json.JsonType;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Certification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer certificationId;
    @Setter
    @Enumerated(EnumType.STRING)
    private CategoryCode categoryCode;
    @Setter
    private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    private String placeName; // 장소 명
    private String description; // 내용

    private String address; // 주소
    private Boolean isHideAddress; // 주소 숨기기 여부 (true: 숨김 , false: 노출) 기본값 : false
    private String geoCode; // 지역 코드
    private String pGeoCode; // 부모 지역 코드
    private String latitude; // 위도
    private String longitude; // 경도

    private String photoUrl; // 사진 URL
    @Setter
    private Boolean isCorrect; // 올바른 사진 여부 ( NCP GreenEye로 체크 )
    private Boolean isAchievements; // 업적 영향 여부 ( 해당 인증이 등록되었을 때 가지게 된 업적이 있는가?)
    @Setter
    private int commentCount; // 댓글 개수
    private Boolean isExpose; // 동네강아지에 노출 시키는 인증 구분

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false)
    private User user;

    @Setter
    @Type(type = "json")
    @Column(columnDefinition = "longtext")
    private List<String> photos;

    public static Certification from(CertCreate certCreate, String address, Code code, User user) {
        return Certification.builder()
                .user(user)
                .categoryCode(certCreate.categoryCode())
                .mungpleId(certCreate.mungpleId())
                .placeName(certCreate.placeName())
                .description(certCreate.description())
                .address(address)
                .geoCode(code.getCode())
                .pGeoCode(code.getPCode())
                .latitude(certCreate.latitude())
                .longitude(certCreate.longitude())
                .isCorrect(true)
                .isHideAddress(certCreate.isHideAddress())
                .commentCount(0)
                .isExpose(user.getRoles().equals("ROLE_USER")) // 관리자가 작성한 글은 동네 강아지에서 보이지 않게 한다.
                .build();
    }

    public static Certification from(CertCreate certCreate, Mungple mungple, User user) {
        return Certification.builder()
                .user(user)
                .categoryCode(mungple.getCategoryCode())
                .mungpleId(certCreate.mungpleId())
                .placeName(mungple.getPlaceName())
                .description(certCreate.description())
                .address(mungple.formattedAddress())
                .geoCode(mungple.getGeoCode())
                .pGeoCode(mungple.getPGeoCode())
                .latitude(mungple.getLatitude())
                .longitude(mungple.getLongitude())
                .isCorrect(true)
                .isHideAddress(false)
                .commentCount(0)
                .isExpose(user.getRoles().equals("ROLE_USER")) // 관리자가 작성한 글은 동네 강아지에서 보이지 않게 한다.
                .build();
    }

    public Certification update(CertUpdate certUpdate) {
        return Certification.builder()
                .certificationId(this.certificationId)
                .user(user)
                .categoryCode(categoryCode)
                .mungpleId(mungpleId)
                .placeName(placeName)
                .address(address)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .latitude(latitude)
                .longitude(longitude)
                .isCorrect(isCorrect)
                .commentCount(commentCount)
                // update
                .description(certUpdate.description())
                .isHideAddress(certUpdate.isHideAddress())
                .build();
    }
}