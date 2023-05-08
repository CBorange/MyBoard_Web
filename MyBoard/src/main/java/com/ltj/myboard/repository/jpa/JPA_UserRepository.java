package com.ltj.myboard.repository.jpa;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPA_UserRepository extends JpaRepository<User, String>, UserRepository {

}
