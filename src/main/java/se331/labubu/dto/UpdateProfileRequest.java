package se331.labubu.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String surname;
    private String profileImage;
}