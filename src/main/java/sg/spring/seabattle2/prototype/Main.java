package sg.spring.seabattle2.prototype;

import sg.spring.seabattle2.domain.GameMap;
import sg.spring.seabattle2.domain.GamePhase;
import sg.spring.seabattle2.domain.ShipPart;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerColor;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGame;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGamePlayer;
import sg.spring.seabattle2.persistence.TwoPlayerGameNode;
import sg.spring.seabattle2.persistence.mapper.TwoPlayerGamePersistenceMapper;
import sg.spring.seabattle2.persistence.mapper.TwoPlayerGamePlayerPersistenceMapper;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Create a new Two-Player Game
        UUID gameId = UUID.randomUUID();
        TwoPlayerGame game = new TwoPlayerGame(gameId);

        // Create Players
        TwoPlayerGamePlayer playerRed = new TwoPlayerGamePlayer();
        playerRed.setColor(TwoPlayerColor.RED);
        playerRed.setName("Player Red");

        TwoPlayerGamePlayer playerBlue = new TwoPlayerGamePlayer();
        playerBlue.setColor(TwoPlayerColor.BLUE);
        playerBlue.setName("Player Blue");

        // Create Game Maps
        int gridSize = 5; // 5x5 grid for testing
        GameMap redMap = new GameMap(gridSize);
        GameMap blueMap = new GameMap(gridSize);

        // Assign maps to players (opponent's view)
        playerRed.setOpponentMap(blueMap);
        playerBlue.setOpponentMap(redMap);

        // Place ships for both players
        placeShipsForTesting(redMap);
        placeShipsForTesting(blueMap);

        // Assign players to the game
        game.setPlayerRed(playerRed);
        game.setPlayerBlue(playerBlue);

        // Test game by hitting a few coordinates
        testGameHits(game);

        TwoPlayerGameNode gameNode = TwoPlayerGamePersistenceMapper.INSTANCE.toEntity(game);
        TwoPlayerGame mappedGame = TwoPlayerGamePersistenceMapper.INSTANCE.toDomain(gameNode);
        System.out.println("test");
    }

    private static void placeShipsForTesting(GameMap map) {
        UUID shipUUID = UUID.randomUUID();

        ShipPart ship1 = new ShipPart(shipUUID);
        ship1.setOtherPartsLocations(new int[]{1, 2});

        ShipPart ship2 = new ShipPart(shipUUID);
        ship2.setOtherPartsLocations(new int[]{0, 2});

        ShipPart ship3 = new ShipPart(shipUUID);
        ship3.setOtherPartsLocations(new int[]{0, 1});

        // Placing a 3-part ship at indexes 0, 1, and 2
        map.placeShipPart(ship1, 0);
        map.placeShipPart(ship2, 1);
        map.placeShipPart(ship3, 2);
    }

    private static void testGameHits(TwoPlayerGame game) {
        System.out.println("Testing Hits on Player Red's Map:");
        System.out.println("Hit at (0,0): " + game.getPlayerBlue().getOpponentMap().hit(0, 0));
        System.out.println("Hit at (1,0): " + game.getPlayerBlue().getOpponentMap().hit(1, 0));
        System.out.println("Hit at (2,0): " + game.getPlayerBlue().getOpponentMap().hit(2, 0));

        System.out.println("Miss at (4,4): " + game.getPlayerBlue().getOpponentMap().hit(4, 4));

        System.out.println("\nTesting Hits on Player Blue's Map:");
        System.out.println("Hit at (0,0): " + game.getPlayerRed().getOpponentMap().hit(0, 0));
    }
}
