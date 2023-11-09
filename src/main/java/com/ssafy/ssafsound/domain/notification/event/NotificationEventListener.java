package com.ssafy.ssafsound.domain.notification.event;

import com.ssafy.ssafsound.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationEvent(NotificationEvent notificationEvent) {
        notificationService.sendNotification(notificationEvent);
    }
}
