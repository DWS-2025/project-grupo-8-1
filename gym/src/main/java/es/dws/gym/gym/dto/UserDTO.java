package es.dws.gym.gym.dto;

public record UserDTO(
    String id,
    String firstName,
    String sureName,
    String telephone,
    String mail,
    String address,
    String password,
    String image,
    String rol
) {}
