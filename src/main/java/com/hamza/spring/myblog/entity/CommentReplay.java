package com.hamza.spring.myblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_replies")
public class CommentReplay {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @ManyToOne
     @JoinColumn(name = "comment_id", nullable = false)
     private Comment comment;

     @Column(nullable = false)
     private String name;

     @Column(nullable = false)
     private String email;

    @Column(nullable = false)
    private String body;
}
