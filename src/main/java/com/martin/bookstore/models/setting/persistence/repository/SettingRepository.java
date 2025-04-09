package com.martin.bookstore.models.setting.persistence.repository;

import com.martin.bookstore.models.setting.persistence.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findByName(String settingName);
}
