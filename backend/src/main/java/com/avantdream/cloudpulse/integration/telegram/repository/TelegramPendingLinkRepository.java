package com.avantdream.cloudpulse.integration.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avantdream.cloudpulse.integration.telegram.entity.TelegramPendingLink;

import java.util.Optional;

@Repository
public interface TelegramPendingLinkRepository extends JpaRepository<TelegramPendingLink, Integer> {
    Optional<TelegramPendingLink> findByCode(String code);
    void deleteByIntegrationId(Integer integrationId);
}
