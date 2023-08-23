package com.ssafy.ssafsound.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="member_login_log")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginLog {

    @Id
    @Column(name = "member_login_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    @Column
    private LocalDateTime loginTime;

    @Column
    private String remoteAddress;

    @Column
    private String clientDevice;

    public static MemberLoginLog ofCreateLoginLog(Member member, String clientDevice, String remoteAddress) {
        return MemberLoginLog.builder()
                .member(member)
                .clientDevice(clientDevice)
                .remoteAddress(remoteAddress)
                .build();
    }
}
