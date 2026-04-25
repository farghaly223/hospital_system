package hospital_system.hospital_system.services;

import hospital_system.hospital_system.dto.AppointmentRequest;
import hospital_system.hospital_system.entities.Appointment;
import hospital_system.hospital_system.repositories.AppointmentRepository;
import hospital_system.hospital_system.repositories.DoctorRepository;
import hospital_system.hospital_system.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Book a new appointment
    public Appointment bookAppointment(AppointmentRequest request) {
        // Check patient exists
        patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("المريض ده مش موجود"));

        // Check doctor exists
        doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("الدكتور ده مش موجود"));

        // Check slot availability (no double booking)
        boolean slotTaken = appointmentRepository.existsByDoctorIdAndAppDate(
                request.getDoctorId(), request.getAppDate());
        if (slotTaken) {
            throw new RuntimeException("الميعاد ده محجوز بالفعل، اختار وقت تاني");
        }

        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setDoctorId(request.getDoctorId());
        appointment.setAppDate(request.getAppDate());
        appointment.setReason(request.getReason());
        appointment.setStatus("PENDING");

        return appointmentRepository.save(appointment);
    }

    // Cancel an appointment
    public Appointment cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الحجز ده مش موجود"));

        if (appointment.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("الحجز ده ملغي بالفعل");
        }
        if (appointment.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("مينفعش تلغي حجز خلص");
        }

        appointment.setStatus("CANCELLED");
        return appointmentRepository.save(appointment);
    }

    // Reschedule an appointment
    public Appointment rescheduleAppointment(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الحجز ده مش موجود"));

        if (appointment.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("مينفعش تعيد جدولة حجز ملغي");
        }
        if (appointment.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("مينفعش تعيد جدولة حجز خلص");
        }

        // Check new slot availability
        boolean slotTaken = appointmentRepository.existsByDoctorIdAndAppDate(
                appointment.getDoctorId(), request.getAppDate());
        if (slotTaken) {
            throw new RuntimeException("الميعاد الجديد ده محجوز بالفعل، اختار وقت تاني");
        }

        appointment.setAppDate(request.getAppDate());
        if (request.getReason() != null) appointment.setReason(request.getReason());
        appointment.setStatus("PENDING");

        return appointmentRepository.save(appointment);
    }

    // Get appointments by patient
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // Get appointments by doctor
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Get all appointments (Admin)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get appointment by id
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الحجز ده مش موجود"));
    }

    // Mark appointment as completed
    public Appointment completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الحجز ده مش موجود"));

        if (appointment.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("مينفعش تكمل حجز ملغي");
        }

        appointment.setStatus("COMPLETED");
        return appointmentRepository.save(appointment);
    }
}
