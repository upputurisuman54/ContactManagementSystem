package com.example.Contact.repository;



import com.example.Contact.model.ActivityLog;
import com.example.Contact.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    Page<ActivityLog> findByUserOrderByTimestampDesc(User user, Pageable pageable);
}