package cat.itacademy.s05.S05.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("player")
public class Player {
    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("wins")
    private int wins;

    public int addWin(){
        this.wins++;
        return this.wins;
    }
}