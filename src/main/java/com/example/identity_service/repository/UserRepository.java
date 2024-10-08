package com.example.identity_service.repository;

import com.example.identity_service.entity.IdenUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<IdenUser, String> {
    public boolean existsByUsername (String username);
}
