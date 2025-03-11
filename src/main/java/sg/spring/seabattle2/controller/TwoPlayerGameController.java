package sg.spring.seabattle2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerColor;
import sg.spring.seabattle2.service.TwoPlayerGameService;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
public class TwoPlayerGameController {

    private final TwoPlayerGameService twoPlayerGameService;

    @Autowired
    public TwoPlayerGameController(TwoPlayerGameService twoPlayerGameService) {
        this.twoPlayerGameService = twoPlayerGameService;
    }

    @GetMapping("game/{id}")
    public String getGame(@PathVariable("id") UUID gameId) {
        return twoPlayerGameService.getGame(gameId).toString();
    }

    @PostMapping("game")
    public String createGame(@RequestBody CreateTwoPlayerGameDTO createTwoPlayerGameDTO) {
        return twoPlayerGameService.createGame(
                createTwoPlayerGameDTO.getRedPlayer(),
                createTwoPlayerGameDTO.getBluePlayer(),
                createTwoPlayerGameDTO.getShipSizes())
                .toString();
    }

    @PostMapping("game/{id}/setup")
    public String setupMap(@PathVariable("id") UUID gameId,
                           @RequestHeader("player-id") TwoPlayerColor playerColor,
                           @RequestBody CreateMapDTO createMapDTO) {
        return twoPlayerGameService.setupMap(gameId, playerColor, createMapDTO.ships).toString();
    }

}
