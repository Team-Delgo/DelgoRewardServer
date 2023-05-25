package com.delgo.reward.repository;


import com.delgo.reward.domain.Mungple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface MungpleRepository extends JpaRepository<Mungple, Integer>, JpaSpecificationExecutor<Mungple> {

    List<Mungple> findByCategoryCode(String categoryCode);

    boolean existsByLatitudeAndLongitude(String latitude, String longitude);

    List<Mungple> findAllByIsActive(boolean isActive);

    List<Mungple> findByCategoryCodeAndIsActive(String categoryCode, boolean isActive);

    @Query("select m from Mungple m where m.isActive = true and m.mungpleId in :mungpleIds")
    List<Mungple> findMungpleByIds(List<Integer> mungpleIds);

    List<Mungple> findByJibunAddress(String address);
}
