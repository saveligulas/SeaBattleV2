package sg.spring.seabattle2.domain.twoplayer;

import lombok.Getter;
import lombok.Setter;
import sg.spring.core.domain.SerialDomainIdentifier;
import sg.spring.seabattle2.domain.GameMap;
import sg.spring.seabattle2.domain.IGamePlayer;
import sg.spring.seabattle2.domain.ShipPart;

@Getter
@Setter
public class TwoPlayerGamePlayer implements IGamePlayer {
    private String id;
    private TwoPlayerColor color;
    private String name;
    private GameMap opponentMap;

    public TwoPlayerGamePlayer() {
    }

    public TwoPlayerGamePlayer(TwoPlayerColor color, String name) {
        this.color = color;
        this.name = name;
    }

    @Override
    public int getGameIdentifier() {
        return color.ordinal();
    }

    @Override
    public GameMap getOpponentMap() {
        return opponentMap;
    }
    
    public boolean hasLost() {
        if (opponentMap == null) {
            return false; // Can't lose if map isn't set up yet
        }
        
        // Check if all ship parts are hit
        for (ShipPart part : opponentMap.getShipParts()) {
            if (part != null && part != ShipPart.MISSED && !part.isHit()) {
                return false; // Found at least one ship part that's not hit
            }
        }
        
        return true; // All ship parts are hit
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player(");
        sb.append("name='").append(name).append("'");
        sb.append(", color=").append(color);
        
        if (id != null) {
            sb.append(", id='").append(id).append("'");
        }
        
        sb.append(", mapStatus=");
        if (opponentMap != null) {
            sb.append("SET_UP");
        } else {
            sb.append("NOT_SET_UP");
        }
        
        sb.append(")");
        return sb.toString();
    }
}
