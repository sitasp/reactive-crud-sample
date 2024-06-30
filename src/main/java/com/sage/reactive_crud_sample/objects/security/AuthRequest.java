package com.sage.reactive_crud_sample.objects.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sage.reactive_crud_sample.objects.security.features.Authable;
import lombok.Data;
import lombok.Setter;


@Data
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthRequest implements Authable<String, String> {
    private String userName;
    private String password;

    @Override
    public String getIdentifier() {
        return userName;
    }

    @Override
    public String getCredential() {
        return password;
    }
}
