package com.osama.warebot.robots;

import com.osama.warebot.common.OnCreate;
import com.osama.warebot.common.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RobotRequestDto {

    @NotBlank(groups = OnUpdate.class, message = "{robot.id.required}")
    private String id;

    @NotBlank(groups = OnCreate.class, message = "{robot.name.required}")
    private String name;

    private Boolean available;

    @NotBlank(groups = OnCreate.class, message = "{robot.status.required}")
    private String status; // idle, busy, maintenance

    private String currentShelfId;
}
