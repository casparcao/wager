package top.mikecao.wager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mikecao.wager.common.Result;
import top.mikecao.wager.entities.Game;
import top.mikecao.wager.services.GameService;

import java.util.List;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/api/games")
    public Result<List<Game>> list(){
        return gameService.list();
    }
}
