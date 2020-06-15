package com.graphSearch.domain.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "user")
@Data
@NoArgsConstructor
public class User {
    @Id

    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

}
