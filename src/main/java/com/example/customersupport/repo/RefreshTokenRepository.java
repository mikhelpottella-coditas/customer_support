package com.example.customersupport.repo;


import com.example.customersupport.entity.RefreshToken;
import com.example.customersupport.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<List<RefreshToken>> findAllByUser(User user);
}