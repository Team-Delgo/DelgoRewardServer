package com.delgo.reward.service.user;

import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.domain.user.CategoryCount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class CategoryCountServiceTest {
    @Autowired
    CategoryCountService categoryCountService;

    @Test
    @Transactional
    void save() {
        // given
        int userId = 99999;
        CategoryCount categoryCount = CategoryCount.from(userId);

        // when
        CategoryCount savedCategoryCount = categoryCountService.save(categoryCount);

        // then
        assertThat(savedCategoryCount.getUserId()).isEqualTo(userId);
    }

    @Test
    @Transactional
    void create() {
        // given
        int userId = 99999;

        // when
        CategoryCount savedCategoryCount = categoryCountService.create(userId);

        // then
        assertThat(savedCategoryCount.getUserId()).isEqualTo(userId);
    }

    @Test
    void getOneByUserId() {
        // given
        int userId = 1;

        // when
        CategoryCount savedCategoryCount = categoryCountService.getOneByUserId(userId);

        // then
        assertThat(savedCategoryCount.getUserId()).isEqualTo(userId);
    }

    @Test
    void getOneByUserId_예외처리() {
        // given
        int userId = 99999;

        // when

        // then
        assertThatThrownBy(() -> categoryCountService.getOneByUserId(userId))
                .isInstanceOf(NotFoundDataException.class);
    }

    @Test
    @Transactional
    void delete() {
        // given
        int userId = 1;

        // when
        categoryCountService.delete(userId);

        // then
        assertThatThrownBy(() -> categoryCountService.getOneByUserId(userId))
                .isInstanceOf(NotFoundDataException.class);
    }
}