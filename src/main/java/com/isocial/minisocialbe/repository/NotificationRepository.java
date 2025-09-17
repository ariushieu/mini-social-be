package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.model.Notification;
import com.isocial.minisocialbe.enums.NotificationType;
import com.isocial.minisocialbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    List<NotificationType> findByRecipientOrderByCreatedAtDesc(User recipient);
    List<Notification> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(User recipient);
}
