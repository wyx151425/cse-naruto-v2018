package com.cse.naruto.repository;

import com.cse.naruto.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WangZhenqi
 */
@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Integer> {

    /**
     * 根据Token获取统计结果
     *
     * @param token 令牌
     * @return 统计结果对象
     */
    Statistic findOneByToken(String token);
}
