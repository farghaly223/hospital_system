package hospital_system.hospital_system.services;

import hospital_system.hospital_system.entities.Doctor;
import hospital_system.hospital_system.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الدكتور ده مش موجود"));
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = getDoctorById(id);
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setPhone(updatedDoctor.getPhone());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setExperienceYears(updatedDoctor.getExperienceYears());
        existingDoctor.setConsultationFee(updatedDoctor.getConsultationFee());
        existingDoctor.setId(updatedDoctor.getId());
        return doctorRepository.save(existingDoctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}