package sg.spring.seabattle2.service;

import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import sg.spring.seabattle2.controller.ShotResultDTO;
import sg.spring.seabattle2.domain.GameMap;
import sg.spring.seabattle2.domain.GamePhase;
import sg.spring.seabattle2.domain.ShipPart;
import sg.spring.seabattle2.domain.ShotResult;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerColor;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGame;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGamePlayer;
import sg.spring.seabattle2.persistence.TwoPlayerGameNode;
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

    private void saveGame(TwoPlayerGame game) {
        try {
            TwoPlayerGameNode node = TwoPlayerGamePersistenceMapper.INSTANCE.toEntity(game);
            twoPlayerGameNodeRepository.save(node);
        } catch (Exception e) {
            throw new RuntimeException("Error saving game state: " + e.getMessage(), e);
        }
    }

    public TwoPlayerGame getGame(UUID gameId) {
        return retrieveGame(gameId);
    }
    
    public GamePhase getGamePhase(UUID gameId) {
        return retrieveGame(gameId).getGamePhase();
    }
    
    public boolean isGameSetupComplete(UUID gameId) {
        return retrieveGame(gameId).isShipsSetup();
    }
    
    public boolean hasPlayerSetupMap(UUID gameId, TwoPlayerColor playerColor) {
        TwoPlayerGame game = retrieveGame(gameId);
        TwoPlayerGamePlayer inversePlayer = game.getInversePlayer(playerColor);
        return inversePlayer.getOpponentMap() != null;
    }
    
    public String getMapForOwner(UUID gameId, TwoPlayerColor playerColor) {
        TwoPlayerGame game = retrieveGame(gameId);
        TwoPlayerGamePlayer inversePlayer = game.getInversePlayer(playerColor);
        
        if (inversePlayer.getOpponentMap() == null) {
            return "Map not set up yet for player " + playerColor;
        }
        
        return "Map for player " + playerColor + " (showing ship locations):\n" + 
               inversePlayer.getOpponentMap().toStringForOwner();
    }
    
    public String getMapForOpponent(UUID gameId, TwoPlayerColor playerColor) {
        TwoPlayerGame game = retrieveGame(gameId);
        TwoPlayerGamePlayer inversePlayer = game.getInversePlayer(playerColor);
        
        if (inversePlayer.getOpponentMap() == null) {
            return "Map not set up yet for player " + playerColor;
        }
        
        return "Map for player " + playerColor + " (opponent view - only showing hits and misses):\n" + 
               inversePlayer.getOpponentMap().toStringForOpponent();
    }
    
    public TwoPlayerColor getCurrentTurn(UUID gameId) {
        TwoPlayerGame game = retrieveGame(gameId);
        return game.getActivePlayerColor();
    }
    
    public TwoPlayerColor getWinner(UUID gameId) {
        TwoPlayerGame game = retrieveGame(gameId);
        if (game.getGamePhase() != GamePhase.END || game.getWinner() == null) {
            return null; // No winner yet
        }
        return game.getWinner().getColor();
    }
    
    public ShotResultDTO fireShot(UUID gameId, TwoPlayerColor playerColor, int x, int y) {
        TwoPlayerGame game = retrieveGame(gameId);
        
        // Validate the game is in PLAY phase
        if (game.getGamePhase() != GamePhase.PLAY) {
            throw new IllegalStateException("Cannot fire: Game is not in PLAY phase. Current phase: " + game.getGamePhase());
        }
        
        // Validate it's the player's turn
        TwoPlayerGamePlayer player = game.getPlayerByColor(playerColor);
        if (!game.isPlayerTurn(player)) {
            throw new IllegalStateException("Cannot fire: It's not your turn. Current turn: " + game.getActivePlayerColor());
        }
        
        // Get the opponent's map (which is on the inverse player)
        TwoPlayerGamePlayer targetPlayer = game.getInversePlayer(playerColor);
        GameMap targetMap = targetPlayer.getOpponentMap();
        if (targetMap == null) {
            throw new IllegalStateException("Cannot fire: Target map is not set up");
        }
        
        // Check if the index is valid
        int index = targetMap.getSizeRoot() * y + x;
        if (index < 0 || index >= targetMap.getShipParts().length) {
            throw new IllegalArgumentException("Shot coordinates out of bounds");
        }
        
        // Fire the shot
        ShotResult result = targetMap.hit(x, y);
        
        // Check if the game has ended after the shot
        game.checkForGameEnd();
        
        // Save the state after hit
        saveGame(game);
        
        // Switch turns only if it was a miss
        boolean turnEnded = false;
        if (result == ShotResult.MISS) {
            game.switchTurn();
            turnEnded = true;
            // Save again after turn switch
            saveGame(game);
        }
        
        // Prepare result
        TwoPlayerColor winner = null;
        if (game.getGamePhase() == GamePhase.END && game.getWinner() != null) {
            winner = game.getWinner().getColor();
        }
        
        return new ShotResultDTO(
                result, 
                turnEnded, 
                game.getActivePlayerColor(),
                game.getGamePhase() == GamePhase.END,
                winner
        );
    }

    public UUID createGame(String redPlayer, String bluePlayer, @Nullable List<Integer> allowedShips) {
        TwoPlayerGame twoPlayerGame = new TwoPlayerGame();
        twoPlayerGame.setPlayerRed(new TwoPlayerGamePlayer(TwoPlayerColor.RED, redPlayer));
        twoPlayerGame.setPlayerBlue(new TwoPlayerGamePlayer(TwoPlayerColor.BLUE, bluePlayer));
        if (allowedShips != null) {
            twoPlayerGame.setAllowedShips(allowedShips);
        }

        saveGame(twoPlayerGame);
        return twoPlayerGame.getIdentifier();
    }

    public GameMap setupMap(UUID gameId, TwoPlayerColor playerColor, List<List<Integer>> ships) {
        TwoPlayerGame twoPlayerGame = retrieveGame(gameId);
        TwoPlayerGamePlayer player = twoPlayerGame.getInversePlayer(playerColor);

        // Create a new map and make sure no existing map is there
        if (player.getOpponentMap() != null) {
            throw new IllegalStateException("Map for " + playerColor + " is already set up");
        }
        
        GameMap map = new GameMap();
        // We don't set relationshipId here, let Neo4j generate it
        
        // Place ships on the map
        for (List<Integer> ship : ships) {
            // Create a ship with a single UUID for all parts of the same ship
            UUID shipId = UUID.randomUUID();
            
            // First place all parts
            for (Integer pos : ship) {
                ShipPart part = new ShipPart(shipId);
                map.placeShipPart(part, pos);
            }
            
            // Then connect all parts
            for (Integer pos : ship) {
                ShipPart part = map.getShipParts()[pos];
                if (part != null) {
                    // Create array of other positions
                    int[] otherPositions = ship.stream()
                            .filter(p -> !p.equals(pos))
                            .mapToInt(Integer::intValue)
                            .toArray();
                    part.setOtherPartsLocations(otherPositions);
                }
            }
        }

        player.setOpponentMap(map);
        
        // Check if both players have set up their maps and advance the game phase if needed
        if (twoPlayerGame.isShipsSetup()) {
            twoPlayerGame.setGamePhase(GamePhase.PLAY);
        }
        
        saveGame(twoPlayerGame);
        return map;
    }
}
