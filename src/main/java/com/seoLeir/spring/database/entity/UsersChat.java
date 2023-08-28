package com.seoLeir.spring.database.entity;


import jakarta.persistence.*;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_chat")
public class UsersChat implements BaseEntity<Long>{


    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;


    public void setUser(User user) {
        this.user = user;
        this.user.getUsersChats().add(this);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        this.chat.getUsersChats().add(this);
    }
}
