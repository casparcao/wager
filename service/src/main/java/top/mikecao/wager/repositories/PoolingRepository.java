package top.mikecao.wager.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.mikecao.wager.entities.Pooling;
import top.mikecao.wager.entities.Rule;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoolingRepository extends MongoRepository<Pooling, Long> {
    Optional<Pooling> findByGameAndRule(long game, Rule rule);

    List<Pooling> findByGame(long gid);
}
