package com.takenote.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.takenote.model.User;
import com.takenote.model.UserStatus;
import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Boolean isDeleted;
    private UserStatus status;


    public static User toUser(UserDTO dto) {
        User.UserBuilder<?, ?> builder = User.builder();

        return builder
                .id(Objects.nonNull(dto.id) ? dto.id : 0)
                .email(dto.email)
                .name(dto.name)
                .password(dto.password)
                .status(dto.status)
                .build();
    }

    public static UserDTO fromUser(User user) {
        UserDTOBuilder builder = UserDTO.builder();

        return builder
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .status(user.getStatus())
                .build();
    }
}
