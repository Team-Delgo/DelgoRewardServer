package com.delgo.reward.repository;


import com.delgo.reward.domain.Mungple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface MungpleRepository extends JpaRepository<Mungple, Integer>, JpaSpecificationExecutor<Mungple> {
    Optional<Mungple> findByMungpleId(int mungpleId);

    Optional<Mungple> findByLatitudeAndLongitude(String latitude, String longitude);

    List<Mungple> findByCategoryCodeAndIsActive(String categoryCode, boolean isActive);

    List<Mungple> findAllByIsActive(boolean isActive);

    @Query(value = "select * from mungple where is_active = true and mungple_id in (select mungple_id from certification group by mungple_id order by count(*) desc) limit ?", nativeQuery = true)
    List<Mungple> findMungpleOfMostCount(int count);
}
