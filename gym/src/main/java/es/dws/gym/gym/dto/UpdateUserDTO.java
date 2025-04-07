package es.dws.gym.gym.dto;

public record UpdateUserDTO(
    String firstName,
    String sureName,
    String telephone,
    String mail,
    String address
) {}
