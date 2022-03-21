package com.exampleProject.signUpSpring.repo;

import com.exampleProject.signUpSpring.domain.AppUser;
import com.exampleProject.signUpSpring.domain.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    <S extends ConfirmationToken> Optional<ConfirmationToken> findByToken(String token);

    @Query("SELECT c from ConfirmationToken as c WHERE c.id = (SELECT MAX(c1.id) FROM ConfirmationToken as c1 WHERE c1.appUser.id = ?1)")
    <S extends ConfirmationToken> Optional<ConfirmationToken> findByUser(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);


}
