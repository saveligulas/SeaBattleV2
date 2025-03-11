package sg.spring.seabattle2.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipPart {
    public static ShipPart MISSED = new ShipPart(true);

    private boolean isHit;
    @Getter
    private int[] otherPartsLocations;
    private int shipId;

    public ShipPart() {
        this(false);
    }

    public ShipPart(boolean isHit) {
        this.isHit = isHit;
    }

    public void hit() {
        if (isHit) {
            throw new ShipPartAlreadyHitException();
        }
        isHit = true;
    }

}
