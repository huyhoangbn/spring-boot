package com.javaspring.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "agentconnection")
@Table(name = "agentconnection")
@EntityListeners(AuditingEntityListener.class)
public class Agentconnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String merchant;
    private String token;
    private int enable;

    @Transient
    public static final int DISNABLE = 1;
    @Transient
    public static final int ENABLE = 0;
}
