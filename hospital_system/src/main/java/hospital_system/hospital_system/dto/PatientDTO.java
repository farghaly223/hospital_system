package hospital_system.hospital_system.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Long id;
    private String name;
    private String phone;
    private String medicalHistory;
}