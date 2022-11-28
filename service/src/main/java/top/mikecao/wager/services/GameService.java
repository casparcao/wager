package top.mikecao.wager.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.mikecao.wager.common.Result;
import top.mikecao.wager.entities.Game;
import top.mikecao.wager.exception.AppServerException;
import top.mikecao.wager.repositories.GameRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Result<List<Game>> list() {
        return Result.ok(gameRepository.findAll(Sort.by(Sort.Order.asc("date"))));
    }

    public List<Game> today() {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String date = format.format(now);
        Date from;
        Date to;
        try {
            from = format2.parse(date + " 00:00:00");
            to = format2.parse(date + " 23:59:59");
        } catch (ParseException e) {
            log.error("日期解析异常>>", e);
            throw new AppServerException();
        }
        return gameRepository.findByDateBetween(from, to);
    }

}
