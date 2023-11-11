package com.delgo.reward.user.service.port;


import com.delgo.reward.user.domain.CategoryCount;

import java.util.Optional;

public interface CategoryCountRepository {
    CategoryCount save(CategoryCount categoryCount);
    Optional<CategoryCount> findByUserId(int userId);
    void deleteByUserId(int userId);
}
