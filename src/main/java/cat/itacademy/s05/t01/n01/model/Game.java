package cat.itacademy.s05.t01.n01.model; // 📌 Ubicación del paquete

import lombok.*; // 📌 Lombok para reducir código repetitivo
import org.springframework.data.annotation.Id; // 📌 `@Id` de Spring Data (NO JPA)
import org.springframework.data.relational.core.mapping.Table; // 📌 Mapeo reactivo (NO JPA)
import java.time.LocalDateTime; // 📌 Para manejar fechas
import java.util.UUID; // 📌 Identificador único
import cat.itacademy.s05.t01.n01.model.enums.GameStatus; // 📌 Importamos el Enum de status

@Data  // 📌 Genera automáticamente `getters`, `setters`, `equals()`, `hashCode()` y `toString()`
@NoArgsConstructor  // 📌 Constructor vacío
@AllArgsConstructor // 📌 Constructor con todos los argumentos
@Builder // 📌 Permite construir objetos usando `Game.builder().status(GameStatus.IN_PROGRESS).build();`
@Table("game") // 📌 Mapeamos a la tabla `game` en MySQL (Reactivo, NO JPA)
public class Game {

    @Id  // 📌 Indica que este es el ID de la tabla (Spring Data)
    private UUID id; // 📌 UUID como identificador único (se guarda como `BINARY(16)` en MySQL)

    private GameStatus status; // 📌 Estado del juego (`IN_PROGRESS`, `FINISHED`)

    private String winner; // 📌 Nombre del ganador (puede ser `null` si la partida sigue en curso)

    private LocalDateTime createdAt; // 📌 Fecha de creación (se guarda como `TIMESTAMP` en MySQL)
}