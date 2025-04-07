package es.dws.gym.gym.dto;

public record CreateReviewDTO (
    String user,
    String content,
    String date
) {}