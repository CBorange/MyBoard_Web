package com.ltj.myboard.repository;

import com.ltj.myboard.domain.FindUserPending;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// 유저정보 찾기 대기 데이터 Redis DB 연결 Repository
@Repository
public interface FindUserPendingRepository extends CrudRepository<FindUserPending, String> {
    FindUserPending findByUniqueLinkParam(String uniqueLinkParam);
}
