package hospital_system.hospital_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hospital_system.hospital_system.entities.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}