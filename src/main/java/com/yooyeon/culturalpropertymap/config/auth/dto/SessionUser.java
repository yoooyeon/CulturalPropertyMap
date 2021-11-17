package com.yooyeon.culturalpropertymap.config.auth.dto;

import com.yooyeon.culturalpropertymap.history.domain.ReadingHistory;
import com.yooyeon.culturalpropertymap.history.domain.SearchHistory;
import com.yooyeon.culturalpropertymap.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)

@NoArgsConstructor
@Entity
@Getter
@Table(name="session_user")
public class SessionUser implements Serializable {
    private static final long serialVersionUID = 178630l;

    private String name, email, picture;
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<SearchHistory> searchHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<ReadingHistory> readingHistories = new ArrayList<>();
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();

    }


}