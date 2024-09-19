package com.tms.tms.repository;

import com.tms.tms.model.SlackMsg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMsgRepository extends JpaRepository<SlackMsg, Long> {
}
