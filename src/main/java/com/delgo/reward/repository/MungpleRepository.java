package com.delgo.reward.repository;


import com.delgo.reward.domain.mungple.Mungple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface MungpleRepository extends JpaRepository<Mungple, Integer>, JpaSpecificationExecutor<Mungple> {

    Mungple findMungpleByPlaceName(String placeName);

    List<Mungple> findMungpleByIsActive(boolean isActive);
    List<Mungple> findMungpleByJibunAddress(String address);
    List<Mungple> findMungpleByCategoryCode(String categoryCode);
    List<Mungple> findMungpleByCategoryCodeAndIsActive(String categoryCode, boolean isActive);

    @Query("select m from Mungple m where m.isActive = true and m.mungpleId in :mungpleIds")
    List<Mungple> findMungpleByIds(List<Integer> mungpleIds);

    boolean existsMungpleByLatitudeAndLongitude(String latitude, String longitude);
}
