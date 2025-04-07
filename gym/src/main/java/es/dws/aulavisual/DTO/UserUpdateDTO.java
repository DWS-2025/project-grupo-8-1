package es.dws.aulavisual.DTO;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateDTO(
    String firstName,
    String sureName,
    String telephone,
    String mail,
    String address,
    MultipartFile image
) {}
