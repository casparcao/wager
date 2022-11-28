package top.mikecao.wager.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.mikecao.wager.entities.Wager;

import java.util.List;

@Repository
public interface WagerRepository extends MongoRepository<Wager, Long> {

    List<Wager> findByGame(long gid);

    List<Wager> findByUserAndGame(long uid, long game);

    List<Wager> findByUser(long uid);
}
