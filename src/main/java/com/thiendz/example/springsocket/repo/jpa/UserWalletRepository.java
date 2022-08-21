package com.thiendz.example.springsocket.repo.jpa;

import com.thiendz.example.springsocket.model.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
}
