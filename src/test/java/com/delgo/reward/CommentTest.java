package com.delgo.reward;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.repository.CertRepository;
import com.delgo.reward.comment.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class CommentTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CertRepository certRepository;

    @Test
    public void makeCommentCountTEST() {
        List<Certification> certifications = certRepository.findAll();
        for(Certification cert : certifications){
            // commentCount 업데이트
            int commentCount = commentRepository.countCommentByCertId(cert.getCertificationId());
            if(commentCount > 0) {
                System.out.println("certId : " + cert.getCertificationId());
                System.out.println("commentCount : " + commentCount);
                cert.setCommentCount(commentCount);
            }
        }
        certRepository.saveAll(certifications);
    }
}