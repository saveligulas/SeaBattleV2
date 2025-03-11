package sg.spring.seabattle2.domain.twoplayer;

import lombok.Getter;
import lombok.Setter;
import sg.spring.core.domain.SerialDomainIdentifier;
import sg.spring.seabattle2.domain.GameMap;
import sg.spring.seabattle2.domain.IGamePlayer;

@Getter
@Setter
public class TwoPlayerGamePlayer extends SerialDomainIdentifier implements IGamePlayer {
    private TwoPlayerColor color;
    private String name;
    private GameMap opponentMap;

    @Override
    public int getGameIdentifier() {
        return color.ordinal();
    }

    @Override
    public GameMap getOpponentMap() {
        return opponentMap;
    }
}
