package com.ssafy.ssafsound.domain.lunch.repository;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LunchRepository extends JpaRepository<Lunch, Long> {

    Optional<Lunch> findLunchById(Long id);

    Optional<List<Lunch>> findAllByCampus_IdAndDate(Integer campusId, LocalDate date);
}
