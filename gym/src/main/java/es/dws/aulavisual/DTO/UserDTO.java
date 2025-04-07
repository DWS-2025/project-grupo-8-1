package es.dws.aulavisual.DTO;

import org.springframework.web.multipart.MultipartFile;

public record UserDTO(
    String id,
    String firstName,
    String sureName,
    String telephone,
    String mail,
    String address,
    String password,
    MultipartFile image
) {}
