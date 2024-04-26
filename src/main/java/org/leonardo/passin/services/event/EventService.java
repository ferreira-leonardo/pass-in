package org.leonardo.passin.services.event;

import lombok.RequiredArgsConstructor;
import org.leonardo.passin.domain.attendee.Attendee;
import org.leonardo.passin.domain.events.Event;
import org.leonardo.passin.domain.events.exceptions.EventNotFoundException;
import org.leonardo.passin.dto.event.EventIdDTO;
import org.leonardo.passin.dto.event.EventRequestDTO;
import org.leonardo.passin.dto.event.EventResponseDTO;
import org.leonardo.passin.repositories.event.EventRepository;
import org.leonardo.passin.services.attendee.AttendeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        Optional<Event> event = this.eventRepository.findById(eventId);

        if (event.isEmpty()) {
            throw new EventNotFoundException("Event not found with id: " + eventId);
        }

        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        return new EventResponseDTO(event.get(), attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event newEvent = new Event();

        BeanUtils.copyProperties(eventRequestDTO, newEvent);

        newEvent.setSlug(this.createSlug(eventRequestDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private String createSlug(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .replaceAll("[^\\p{IsAlphabetic}\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}