package uz.pdp.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    public String email;
    public String fullName;
    public String password;
}
