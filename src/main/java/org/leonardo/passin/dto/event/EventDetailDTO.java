package org.leonardo.passin.dto.event;

public record EventDetailDTO(
        String id,
        String title,
        String slug,
        Integer maximumAttendees,
        Integer attendeesAmount
) {
}
