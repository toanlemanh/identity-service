package com.example.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    // User thong thuong chi co the thay doi 3 gia tri nay
    // neu dung api update
//    @Size(min = 8, message = "INVALID_PASSWORD")
//    String password;
    // doi password lam mot tinh nang moi
    String firstName;
    String lastName;
    LocalDate dob;
//    cap nhat role cung o mot tinh nang moi, chi danh cho admin
//    Set<String> roles;

}
