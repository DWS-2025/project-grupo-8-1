package es.dws.gym.gym.dto;

public record CreateUserDTO(
    String id,
    String firstName,
    String sureName,
    String telephone,
    String mail,
    String address,
    String password
) {}
