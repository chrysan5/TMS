package com.tms.tms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("is_delete = false")
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_slackMessages")
public class SlackMsg extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slack_msg_id")
    private Long slackMsgId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String slackMsg;

    public SlackMsg(String slackMsg){
        this.slackMsg = slackMsg;
    }
}
