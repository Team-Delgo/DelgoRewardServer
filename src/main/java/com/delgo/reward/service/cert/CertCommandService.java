package com.delgo.reward.service.cert;


import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.service.ReactionService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertCommandService {
    private final CertRepository certRepository;

    // Service
    private final CodeService codeService;
    private final UserService userService;
    private final GeoDataService geoDataService;
    private final ReactionService reactionService;
    private final MongoMungpleService mongoMungpleService;
    private final ObjectStorageService objectStorageService;

    public Certification create(CertRecord record) {
        User user = userService.getUserById(record.userId());
        String address = geoDataService.getReverseGeoData(record.latitude(), record.longitude());
        Code code = codeService.getGeoCodeByAddress(address);

        return certRepository.save(record.toEntity(address, code, user));
    }

    public Certification createByMungple(CertRecord record) {
        User user = userService.getUserById(record.userId());
        MongoMungple mongoMungple = mongoMungpleService.getMungpleByMungpleId(record.mungpleId());
        return certRepository.save(record.toEntity(mongoMungple, user));
    }

    public Certification modifyCert(Certification certification, ModifyCertRecord record) {
        return certification.modify(record);
    }


    public void changeIsCorrect(int certificationId, boolean isCorrect) {
        Certification cert = certRepository.findOneByCertificationId(certificationId)
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certificationId));
        cert.setIsCorrect(isCorrect);
    }

    public void deleteCert(int certificationId) {
        certRepository.deleteById(certificationId);
        reactionService.deleteByCertId(certificationId);
        objectStorageService.deleteObject(BucketName.CERTIFICATION, certificationId + "_cert.webp");
    }
    }
