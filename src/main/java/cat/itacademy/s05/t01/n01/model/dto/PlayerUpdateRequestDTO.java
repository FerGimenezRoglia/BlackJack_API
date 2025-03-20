package cat.itacademy.s05.t01.n01.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO para actualizar el nombre del jugador.
 * Evita exponer toda la entidad Player en el request.
 *
 * @author Fer_Develop
 */
@Data
@NoArgsConstructor
public class PlayerUpdateRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

}

