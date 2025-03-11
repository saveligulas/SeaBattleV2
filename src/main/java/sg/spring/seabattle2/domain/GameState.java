package sg.spring.seabattle2.domain;

import lombok.Getter;
import sg.spring.core.domain.UUIDDomainIdentifier;

import java.util.UUID;

@Getter
public abstract class GameState extends UUIDDomainIdentifier {
    protected final UUID uuid;
    protected GamePhase gamePhase;

    protected GameState(UUID uuid) {
        this.uuid = uuid;
    }

    public abstract boolean isPlayerTurn(IGamePlayer gamePlayer);
    public abstract boolean isShipsSetup();
}
