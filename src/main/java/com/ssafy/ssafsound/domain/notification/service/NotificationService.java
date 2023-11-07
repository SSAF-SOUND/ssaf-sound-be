package com.ssafy.ssafsound.domain.notification.service;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public Void getNotifications() {
        log.info("알림 조회 서비스 실행");
        List<Notification> notifications = notificationRepository.findAll();

        for (Notification notification : notifications) {
            log.info(notification.getId());
        }

        return null;
    }
}
