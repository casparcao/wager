package top.mikecao.wager.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.mikecao.wager.entities.Game;

import java.util.Date;
import java.util.List;

@Repository
public interface GameRepository extends MongoRepository<Game, Long> {
    List<Game> findByDateBetween(Date sd, Date ed);

}
