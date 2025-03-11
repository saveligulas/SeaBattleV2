package sg.spring.seabattle2.domain;

import lombok.Getter;
import lombok.Setter;
import sg.spring.core.domain.UUIDDomainIdentifier;

import java.util.UUID;

@Getter
@Setter
public class ShipPart extends UUIDDomainIdentifier {
    public static ShipPart MISSED = new ShipPart(true);
    private String elementId;

    public ShipPart(UUID uuid) {
        super(uuid);
    }

    private boolean isHit;
    @Getter
    private int[] otherPartsLocations;

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
