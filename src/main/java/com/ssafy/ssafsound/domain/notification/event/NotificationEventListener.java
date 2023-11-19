package com.ssafy.ssafsound.domain.notification.event;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.service.NotificationService;
import com.ssafy.ssafsound.domain.notification.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationEventListener extends AbstractMongoEventListener<Notification> {
    private final NotificationService notificationService;
    private final SequenceGeneratorService sequenceGeneratorService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationEvent(NotificationEvent notificationEvent) {
        notificationService.sendNotification(notificationEvent);
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Notification> event) {
        event.getSource().setId(sequenceGeneratorService.generateSequence(Notification.SEQUENCE_NAME));
        event.getSource().setCreatedAt(LocalDateTime.now());
    }
}
