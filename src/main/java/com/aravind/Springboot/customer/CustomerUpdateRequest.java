package com.aravind.Springboot.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age

){
}
