package es.dws.gym.gym.dto;

public record ViewUserDTO(
    String id,
    String firstName,
    String sureName,
    String telephone,
    String mail,
    String address,
    String image
) {}
