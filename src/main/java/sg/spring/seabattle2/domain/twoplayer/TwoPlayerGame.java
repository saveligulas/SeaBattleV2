package sg.spring.seabattle2.domain.twoplayer;

import lombok.Getter;
import lombok.Setter;
import sg.spring.seabattle2.domain.GameState;
import sg.spring.seabattle2.domain.IGamePlayer;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TwoPlayerGame extends GameState {
    private TwoPlayerGamePlayer playerRed;
    private TwoPlayerGamePlayer playerBlue;
    private List<Integer> allowedShips;

    public TwoPlayerGame(UUID uuid) {
        super(uuid);
    }


    @Override
    public boolean isPlayerTurn(IGamePlayer gamePlayer) {
        return false;
    }

    @Override
    public boolean isShipsSetup() {
        return (playerRed.getOpponentMap() == null || playerBlue.getOpponentMap() == null);
    }
}
