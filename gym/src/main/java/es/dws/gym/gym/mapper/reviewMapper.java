package es.dws.gym.gym.mapper;

import org.mapstruct.Mapper;

import es.dws.gym.gym.dto.ReviewDTO;
import es.dws.gym.gym.model.Review;

@Mapper(componentModel = "spring")
public interface reviewMapper {

    // This method converts a Review entity to a ReviewDTO object.
    ReviewDTO toDTO(Review review);
}
