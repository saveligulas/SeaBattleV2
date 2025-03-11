package sg.spring.seabattle2.domain;

import lombok.Getter;
import sg.spring.core.domain.SerialDomainIdentifier;

@Getter
public class GameMap extends SerialDomainIdentifier {
    private final ShipPart[] shipParts;
    private final int sizeRoot;

    public GameMap() {
        this(8);
    }

    public GameMap(int sizeRoot) {
        shipParts = new ShipPart[sizeRoot * sizeRoot];
        this.sizeRoot = sizeRoot;
    }

    public ShotResult hit(int x, int y){
        return hit(y * (int) Math.sqrt(shipParts.length) + x);
    }

    public ShotResult hit(int index) {
        ShipPart shipPart = shipParts[index];

        if (shipPart != null) {
            shipPart.hit();
            boolean isSunk = true;
            for (int i : shipPart.getOtherPartsLocations()) {
                if (!shipParts[i].isHit()) {
                    isSunk = false;
                    break;
                }
            }
            return isSunk ? ShotResult.HIT_SUNK : ShotResult.HIT;
        }

        shipParts[index] = ShipPart.MISSED;
        return ShotResult.MISS;
    }

    public void placeShipPart(ShipPart shipPart, int index) {
        shipParts[index] = shipPart;
    }
}
