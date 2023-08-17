package com.ltj.myboard.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * 유저정보 찾기 대기 데이터 Redis에 저장시키는 Redis DB 연결 Repository
 * Spring Data 의 Repository 인터페이스(CRUDRepository 등) 안쓰고 RedisTemplate 사용하여 low Level API로 직접 구현한다.
 * CRUDRepository 사용하여 어노테이션으로 처리하면 필요이상의 key 데이터(Sets, sub index 등)이 생긴다.
 * 해당 Key들의 timeToLive를 적용하려면 KeySpace Changed Notification Event를 받아서 메인 Key가 삭제되었을 때 같이 처리해줘야 하는데
 * 필요이상의 복잡도가 생기는것 같아 단순하게 RedisTemplate ValueOperations(Strings 자료구조 Key Value)로 처리한다.*/
@Repository
@RequiredArgsConstructor
public class FindUserRequestRepository {
    private final RedisTemplate redisTemplate;

    public String findByUniqueLink(String uniqueLink){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String userId = operations.get(uniqueLink);
        if(userId == null)
            throw new NoSuchElementException("유저정보 찾기 요청 데이터 Repository find 실패, " + uniqueLink + " key가 존재하지 않음");
        return userId;
    }

    public void save(String uniqueLink, String userId, long timeToLive){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(uniqueLink, userId, timeToLive, TimeUnit.SECONDS);
    }

    public void remove(String uniqueLink){
        redisTemplate.delete(uniqueLink);
    }
}
