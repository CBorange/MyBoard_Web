package com.ltj.myboard.repository;
import com.ltj.myboard.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByEmail(String email);
    long countById(String id);
    long countByEmail(String email);
    long countByNickname(String nickname);
    long countByEmailAndIdNot(String email, String id);
    long countByNicknameAndIdNot(String nickname, String id);
}
