package com.ssafy.ssafsound.domain.lunch.repository;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LunchRepository extends JpaRepository<Lunch, Long> {

    Optional<Lunch> findById(Long id);

    @Query(value = "select distinct(l) from lunch l left join fetch l.lunchPolls where l.campus = :campus and l.createdAt = :date")
    Optional<List<Lunch>> findAllByCampusAndDate(@Param("campus") MetaData campus, @Param("date") LocalDate date);
}
