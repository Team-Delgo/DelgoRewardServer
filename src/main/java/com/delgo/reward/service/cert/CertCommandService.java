package com.delgo.reward.service.cert;


import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.mongoService.MungpleService;
import com.delgo.reward.record.certification.CertCreate;
import com.delgo.reward.record.certification.CertUpdate;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.service.PhotoService;
import com.delgo.reward.service.ReactionService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertCommandService {
    private final CertRepository certRepository;

    // Service
    private final CodeService codeService;
    private final UserService userService;
    private final PhotoService photoService;
    private final GeoDataService geoDataService;
    private final ReactionService reactionService;
    private final MungpleService mungpleService;
    private final ObjectStorageService objectStorageService;

    public Certification save(Certification certification) {
        return certRepository.save(certification);
    }

    public Certification create(CertCreate certCreate, List<MultipartFile> photoList) {
        User user = userService.getUserById(certCreate.userId());
        String address = geoDataService.getReverseGeoData(certCreate.latitude(), certCreate.longitude());
        Code code = codeService.getGeoCodeByAddress(address);

        Certification certification = certRepository.save(Certification.from(certCreate, address, code, user));
        certification.setPhotos(createPhotoList(certification.getCertificationId(), photoList));
        return certification;
    }

    public Certification createByMungple(CertCreate certCreate, List<MultipartFile> photoList) {
        User user = userService.getUserById(certCreate.userId());
        Mungple mungple = mungpleService.getMungpleByMungpleId(certCreate.mungpleId());

        Certification certification = certRepository.save(Certification.from(certCreate, mungple, user));
        certification.setPhotos(createPhotoList(certification.getCertificationId(), photoList));
        return certification;
    }

    public List<String> createPhotoList(int certificationId, List<MultipartFile> photoList){
        AtomicInteger autoGeneratedId = new AtomicInteger(1);
        return photoList.stream()
                .map(photo -> {
                    String fileName = photoService.makeCertFileName(certificationId, photo, autoGeneratedId.incrementAndGet());
                    return photoService.save(fileName, photo);
                }).toList();
    }

    public Certification update(CertUpdate certUpdate) {
        Certification cert = certRepository.findOneByCertificationId(certUpdate.certificationId())
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certUpdate.certificationId()));
        return certRepository.save(cert.update(certUpdate));
    }

    public void changeIsCorrect(int certificationId, boolean isCorrect) {
        Certification cert = certRepository.findOneByCertificationId(certificationId)
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certificationId));
        cert.setIsCorrect(isCorrect);
    }

    public void delete(int certificationId) {
        certRepository.deleteById(certificationId);
        reactionService.deleteByCertId(certificationId);
        objectStorageService.deleteObject(BucketName.CERTIFICATION, certificationId + "_cert.webp");
    }
}
