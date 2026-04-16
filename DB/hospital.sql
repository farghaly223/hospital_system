create database hospital;
-- 2. جدول المستخدمين (Identity Layer)
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','DOCTOR','NURSE','EMPLOYEE','PATIENT') NOT NULL,
  `is_active` boolean DEFAULT true,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. جدول الدكاترة (Doctor Module)
CREATE TABLE `doctors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint UNIQUE, -- ربط مع حساب المستخدم
  `name` varchar(100) NOT NULL,
  `specialization` varchar(100) NOT NULL, -- تخصص الدكتور
  `phone` varchar(20),
  `email` varchar(100),
  `experience_years` int,
  `consultation_fee` double DEFAULT 0.0, -- سعر الكشف
  PRIMARY KEY (`id`),			
  CONSTRAINT `fk_doctor_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 

-- 4. جدول المرضى (Patient Module)
CREATE TABLE `patients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint UNIQUE, -- ربط مع حساب المستخدم
  `name` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `gender` enum('MALE','FEMALE','OTHER'),
  `date_of_birth` date,
  `blood_group` varchar(5),
  `address` varchar(255),
  `medical_history` text,
  `emergency_contact` varchar(20), -- رقم للطوارئ
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_patient_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. جدول المواعيد (Appointment Module)
CREATE TABLE `appointments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `app_date` datetime NOT NULL,
  `reason` varchar(255), -- سبب الزيارة
  `status` enum('PENDING','CONFIRMED','CANCELLED','COMPLETED') DEFAULT 'PENDING',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_app_patient` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_app_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. جدول الفواتير (Billing Module)
CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint UNIQUE, -- كل حجز له فاتورة واحدة
  `total_amount` double NOT NULL,
  `tax` double DEFAULT 0.0,
  `payment_method` enum('CASH','CARD','INSURANCE') DEFAULT 'CASH',
  `payment_status` enum('PAID','UNPAID','PARTIALLY_PAID') DEFAULT 'UNPAID',
  `issued_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_invoice_app` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE patients MODIFY COLUMN gender VARCHAR(255);

	-- use hospital;
	-- select * from doctors d ;
	-- select * from users u ;
	-- select * from patients p ;
	-- INSERT INTO users (username, password, role) VALUES ('Farghly', '123', 'DOCTOR');
	-- TRUNCATE TABLE users;
	-- SET FOREIGN_KEY_CHECKS = 0; -- قفل حارس البوابة مؤقتاً
    -- DELETE FROM doctors;        -- امسح اللي أنت عايزه
    -- SET FOREIGN_KEY_CHECKS = 1;
