package es.dws.gym.gym.dto;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public record GymClassDTO(
    Long id,
    String name,
    String descript,
    Date time,
    Time duration,
    List<String> users
) {}
