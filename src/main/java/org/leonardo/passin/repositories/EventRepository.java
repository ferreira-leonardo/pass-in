package org.leonardo.passin.repositories;

import org.leonardo.passin.domain.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}
