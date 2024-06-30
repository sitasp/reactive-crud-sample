package com.sage.reactive_crud_sample.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sage.reactive_crud_sample.annotations.CustomEnumeration;
import com.sage.reactive_crud_sample.objects.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.reactive.config.EnableWebFlux;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "user_")
public class User {
    @Id
    @Column("uid")
    private Long uid;
    @Column("username")
    private String username;
    @Column("password")
    private String password;
    @Column("mobile_number")
    private String mobileNumber;
    @Column("email")
    private String email;
    @Column("role")
    @CustomEnumeration
    private Role role;
}
