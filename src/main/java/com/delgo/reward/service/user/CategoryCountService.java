package com.delgo.reward.service.user;

import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.repository.CategoryCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryCountService {
    private final CategoryCountRepository categoryCountRepository;

    public CategoryCount save(CategoryCount categoryCount) {
        return categoryCountRepository.save(categoryCount);
    }

    public CategoryCount create(int userId){
        return categoryCountRepository.save(CategoryCount.from(userId));
    }

    public CategoryCount getOneByUserId(int userId) {
        return categoryCountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundDataException("[CategoryCount] userId : " + userId));
    }

    public void delete(int userId){
        categoryCountRepository.deleteByUserId(userId);
    }
}
