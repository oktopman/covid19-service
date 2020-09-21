package me.oktop.covid19service.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientBoard, Long> {
    List<PatientBoard> findAllByOccurDate(LocalDate occurDate);
    boolean existsByOccurDate(LocalDate occurDate);
    List<PatientBoard> findAllByOccurDateGreaterThanEqualAndOccurDateLessThanEqualOrderByOccurDateDesc(
            LocalDate startDate, LocalDate endDate);
    Optional<PatientBoard> findByOccurDate(LocalDate occurDate);
}
