package com.delgo.reward.certification.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CertPhotoTest {
    @Test
    public void from으로_CertPhoto를_생성할_수_있다() {
        // given
        int certificationId = 1;
        String url = "https://www.test.delgo.pet";

        // when
        CertPhoto photo = CertPhoto.from(certificationId, url);

        // then
        assertThat(photo.getCertPhotoId()).isNull();
        assertThat(photo.getCertificationId()).isEqualTo(certificationId);
        assertThat(photo.getUrl()).isEqualTo(url);
        assertThat(photo.getIsCorrect()).isEqualTo(true); // 기본값 True
    }

    @Test
    public void setUrl로_URL을_변경할_수_있다() {
        // given
        int certificationId = 1;
        String url = "https://www.test.delgo.pet";
        String changeUrl = "https://www.test.delgo.change";

        // when
        CertPhoto photo = CertPhoto.from(certificationId, url);
        photo.setUrl(changeUrl);

        // then
        assertThat(photo.getUrl()).isEqualTo(changeUrl);
    }

    @Test
    public void setIsCorrect로_isCorrect_값을_변경할_수_있다(){
        // given
        int certificationId = 1;
        String url = "https://www.test.delgo.pet";

        // when
        CertPhoto photo = CertPhoto.from(certificationId, url);
        photo.setIsCorrect(false);

        // then
        assertThat(photo.getIsCorrect()).isEqualTo(false);
    }
}
