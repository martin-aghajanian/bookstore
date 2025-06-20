package com.martin.bookstore.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserAccessRequestDto {
    private Long userId;
    private String type;
    private String action;
    private String value;
}
