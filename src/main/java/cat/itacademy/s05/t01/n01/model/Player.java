package cat.itacademy.s05.t01.n01.model; // 📌 Ubicación del paquete

import lombok.*; // 📌 Importamos Lombok para reducir código repetitivo
import org.springframework.data.annotation.Id; // 📌 NOTA: En R2DBC, `@Id` NO es de JPA, sino de Spring Data
import org.springframework.data.relational.core.mapping.Table; // 📌 Mapeo reactivo (NO JPA)
import java.util.UUID;

@Data  // 📌 Genera automáticamente `getters`, `setters`, `equals()`, `hashCode()` y `toString()`
@NoArgsConstructor  // 📌 Genera un constructor vacío
@AllArgsConstructor // 📌 Genera un constructor con todos los argumentos
@Builder // 📌 Permite construir objetos usando `Player.builder().name("Juan").build();`
@Table("player") // 📌 📌 Mapeamos a la tabla `player` en MySQL (Reactivo, NO JPA)
public class Player {

    @Id  // 📌 Indica que este es el ID de la tabla (Spring Data)
    private UUID id; // 📌 UUID como identificador único (se guarda como `BINARY(16)` en MySQL)

    private String name; // 📌 Nombre del jugador (se almacena como VARCHAR(150))

    private int totalGames; // 📌 Contador de partidas jugadas

    private int totalWins; // 📌 Contador de partidas ganadas
}