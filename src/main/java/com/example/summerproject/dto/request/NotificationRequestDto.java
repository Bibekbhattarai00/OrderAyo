package com.example.summerproject.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDto {
    Long id;
    String title;
    String description;
    String createdDate;
    Boolean status;

}
