package sg.spring.seabattle2.service;

import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import sg.spring.seabattle2.domain.GameMap;
import sg.spring.seabattle2.domain.ShipPart;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerColor;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGame;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGamePlayer;
import sg.spring.seabattle2.persistence.mapper.TwoPlayerGamePersistenceMapper;
import sg.spring.seabattle2.persistence.repo.TwoPlayerGameNodeRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TwoPlayerGameService {

    //TODO: abstract this layer so service doesnt have to map this directly
    private final TwoPlayerGameNodeRepository twoPlayerGameNodeRepository;

    public TwoPlayerGameService(TwoPlayerGameNodeRepository twoPlayerGameNodeRepository) {
        this.twoPlayerGameNodeRepository = twoPlayerGameNodeRepository;
    }

    private TwoPlayerGame retrieveGame(UUID gameId) {
        return TwoPlayerGamePersistenceMapper.INSTANCE.toDomain(twoPlayerGameNodeRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Two player game with id: " + gameId + " not found")));
    }

    public TwoPlayerGame getGame(UUID gameId) {
        return retrieveGame(gameId);
    }

    public UUID createGame(String redPlayer, String bluePlayer, @Nullable List<Integer> allowedShips) {
        TwoPlayerGame twoPlayerGame = new TwoPlayerGame();
        twoPlayerGame.setPlayerRed(new TwoPlayerGamePlayer(TwoPlayerColor.RED, redPlayer));
        twoPlayerGame.setPlayerBlue(new TwoPlayerGamePlayer(TwoPlayerColor.BLUE, bluePlayer));
        if (allowedShips != null) {
            twoPlayerGame.setAllowedShips(allowedShips);
        }


        twoPlayerGameNodeRepository.save(TwoPlayerGamePersistenceMapper.INSTANCE.toEntity(twoPlayerGame));
        return twoPlayerGame.getUuid();
    }

    public GameMap setupMap(UUID gameId, TwoPlayerColor playerColor, List<List<Integer>> ships) {
        TwoPlayerGame twoPlayerGame = retrieveGame(gameId);
        TwoPlayerGamePlayer player = twoPlayerGame.getInversePlayer(playerColor);

        //creates map with standard size 8 for now
        GameMap map = new GameMap();
        for (List<Integer> ship : ships) {
            //UUID used for ease of use, can be changed in future
            UUID shipId = UUID.randomUUID();
            for (Integer i : ship) {
                map.placeShipPart(new ShipPart(shipId), i);
            }
        }

        player.setOpponentMap(map);

        twoPlayerGameNodeRepository.save(TwoPlayerGamePersistenceMapper.INSTANCE.toEntity(twoPlayerGame));
        return map;



    }


}
