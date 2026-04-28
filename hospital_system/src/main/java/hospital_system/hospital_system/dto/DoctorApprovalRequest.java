package hospital_system.hospital_system.dto;

public class DoctorApprovalRequest {
    private Long doctorId;
    private boolean approved;
    private Double consultationFee;

    public DoctorApprovalRequest() {}

    public DoctorApprovalRequest(Long doctorId, boolean approved, Double consultationFee) {
        this.doctorId = doctorId;
        this.approved = approved;
        this.consultationFee = consultationFee;
    }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
}
