package com.wetube.wetube_service.dto.post;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollOptionDto {
    private UUID optionId;     // trả về
    private String optionText; // gửi lên khi tạo post+poll
    private Integer voteCount; // trả về
    private Double percentage; // trả về
}


