package me.oktop.covid19service.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository extends JpaRepository<PatientBoard, Long> {
    List<PatientBoard> findAllByCreatedDate(LocalDate createdDate);
    boolean existsByCreatedDate(LocalDate createdDate);
}
