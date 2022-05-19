package io.rently.userservice.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    @Id
    @Column(updatable = false, nullable = false, unique = true)
    private String id;
    @Column(updatable = false, nullable = false, columnDefinition = "TEXT")
    private String providerId;
    @Column(updatable = false, nullable = false, columnDefinition = "TEXT")
    private String provider;
    @Column(columnDefinition = "TEXT")
    private String name;
    @Column(columnDefinition = "TEXT")
    private String email;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String createdAt;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String updatedAt;
}