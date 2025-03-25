package cat.itacademy.s05.S05.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayerNameRequest {
    @NotBlank(message = "Player name cannot be empty.")
    private String playerName;
}
