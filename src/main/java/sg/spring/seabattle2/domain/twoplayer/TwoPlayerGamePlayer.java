package sg.spring.seabattle2.domain.twoplayer;

import sg.spring.seabattle2.domain.GameMap;
import sg.spring.seabattle2.domain.IGamePlayer;

public class TwoPlayerGamePlayer implements IGamePlayer {
    private TwoPlayerColor playerColor;
    private String playerName;
    private GameMap opponentMap;

    @Override
    public int getGameIdentifier() {
        return playerColor.ordinal();
    }

    @Override
    public GameMap getOpponentMap() {
        return opponentMap;
    }
}
