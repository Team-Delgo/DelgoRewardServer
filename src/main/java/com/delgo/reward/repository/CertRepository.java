package com.delgo.reward.repository;


import com.delgo.reward.domain.certification.Certification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {
    List<Certification> findByUserId(int userId);
    void deleteAllByUserId(int userId);

    // 페이징
    Slice<Certification> findByUserId(int userId, Pageable pageable);
    Slice<Certification> findByUserIdAndCategoryCode(int userId, String categoryCode, Pageable pageable);

    @Query(value = "SELECT * FROM certification where is_expose = true order by RAND() limit ?",nativeQuery = true)
    List<Certification> findByIsExpose(int count);

    @Query(value = "SELECT * FROM certification where p_geo_code = ? order by RAND() limit ?", nativeQuery = true)
    List<Certification> findByPGeoCode(String pGeoCode, int count);

    @Query(value = "SELECT * FROM certification where geo_code = ? order by RAND() limit ?", nativeQuery = true)
    List<Certification> findByGeoCode(String geoCode, int count);

    @Query(value = "SELECT * FROM certification where p_geo_code = ? and not geo_code = ? order by RAND() limit ?", nativeQuery = true)
    List<Certification> findByPGeoCodeExceptGeoCode(String pGeoCode, String geoCode, int count);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?) and is_correct_photo = true", nativeQuery = true)
    Slice<Certification> findAllByPaging(int userId, Pageable pageable);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?) and certification_id != ? and is_correct_photo = true", nativeQuery = true)
    Slice<Certification> findAllExcludeSpecificCert(int userId, int certificationId, Pageable pageable);

    @Query(value = "select * from certification where mungple_id = ? and is_correct_photo = true", nativeQuery = true)
    Slice<Certification> findMungpleByPaging(int mungpleId, Pageable pageable);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?) and is_correct_photo = true order by regist_dt desc limit ?", nativeQuery = true)
    List<Certification> findRecentCert(int userId, int count);

    @Query(value = "select count(category_code) from certification where user_id = ? and category_code = ? and mungple_id = ?", nativeQuery = true)
    Integer countByCategory(int userId, String categoryCode, int mungpleId);

    Integer countByUserId(int userId);

    @Query(value = "select count(*) from certification where user_id = ? and mungple_id != 0", nativeQuery = true)
    Integer countOfMungpleByUser(int userId);
}