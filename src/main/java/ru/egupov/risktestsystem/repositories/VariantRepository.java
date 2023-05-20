package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egupov.risktestsystem.models.Variant;

public interface VariantRepository extends JpaRepository<Variant, Integer> {
}