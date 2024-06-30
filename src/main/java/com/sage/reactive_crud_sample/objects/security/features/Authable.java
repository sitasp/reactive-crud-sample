package com.sage.reactive_crud_sample.objects.security.features;

public interface Authable<T, U> {
    T getIdentifier();
    U getCredential();
}
