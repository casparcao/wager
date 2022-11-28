package top.mikecao.wager.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 参与用户
 */
@Data
@Accessors(chain = true)
@Document("user")
public class User {
    private long id;
    @Indexed(unique = true, name = "uk_uname")
    private String username;
    private String password;
}
