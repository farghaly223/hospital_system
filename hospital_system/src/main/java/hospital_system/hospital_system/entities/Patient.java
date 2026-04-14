package hospital_system.hospital_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "الاسم ميبقاش فاضي يا درش")
    @Size(min = 3, max = 100, message = "الاسم لازم يكون بين 3 لـ 100 حرف")
    private String name;

    @NotBlank(message = "رقم التليفون مطلوب")
    @Pattern(regexp = "^01[0125]\\d{8}$", message = "رقم التليفون المصري لازم يكون 11 رقم ويبدأ بـ 010 أو 011 أو 012 أو 015")
    private String phone;

    @NotBlank(message = "تحديد النوع مطلوب")
    private String gender;

    @NotNull(message = "تاريخ الميلاد مطلوب")
    @Past(message = "تاريخ الميلاد لازم يكون في الماضي")
    private LocalDate date_of_birth;

    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "فصيلة الدم غير صحيحة (مثال: A+, O-, AB+)")
    private String blood_group;

    @Size(max = 255, message = "العنوان طويل زيادة عن اللزوم")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;

    // Constructors
    public Patient() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getDate_of_birth() { return date_of_birth; }
    public void setDate_of_birth(LocalDate date_of_birth) { this.date_of_birth = date_of_birth; }
    public String getBlood_group() { return blood_group; }
    public void setBlood_group(String blood_group) { this.blood_group = blood_group; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
}