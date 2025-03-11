package sg.spring.seabattle2.domain;

public class GameMap {
    private final ShipPart[] shipParts;

    public GameMap(int sizeRoot) {
        shipParts = new ShipPart[sizeRoot * sizeRoot];
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

}
