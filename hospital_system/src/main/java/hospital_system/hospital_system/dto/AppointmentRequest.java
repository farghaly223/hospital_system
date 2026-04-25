package hospital_system.hospital_system.dto;

import java.time.LocalDateTime;

public class AppointmentRequest {

    private Long patientId;
    private Long doctorId;
    private LocalDateTime appDate;
    private String reason;

    public AppointmentRequest() {}

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public LocalDateTime getAppDate() { return appDate; }
    public void setAppDate(LocalDateTime appDate) { this.appDate = appDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
