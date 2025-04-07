package es.dws.gym.gym.dto;

public record ReviewDTO (
    Long id,
    String user,
    String content,
    String date
) {}
