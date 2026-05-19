package com.avantdream.cloudpulse.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avantdream.cloudpulse.telegram.entity.TelegramPendingLink;

import java.util.Optional;

@Repository
public interface TelegramPendingLinkRepository extends JpaRepository<TelegramPendingLink, Integer> {
    Optional<TelegramPendingLink> findByCode(String code);
}
