package com.ltj.myboard.repository.redis;

import com.ltj.myboard.domain.FindUserRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// 유저정보 찾기 대기 데이터 Redis DB 연결 Repository
public interface FindUserRequestRepository extends CrudRepository<FindUserRequest, String> {
    FindUserRequest findByUniqueLink(String uniqueLink);
}
