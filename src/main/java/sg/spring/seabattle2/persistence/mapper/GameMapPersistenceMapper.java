package sg.spring.seabattle2.persistence.mapper;

import org.jspecify.annotations.Nullable;
import sg.spring.core.mapper.IDomainPersistenceMapper;
import sg.spring.seabattle2.domain.GameMap;
import sg.spring.seabattle2.domain.ShipPart;
import sg.spring.seabattle2.persistence.GameMapNode;
import sg.spring.seabattle2.persistence.ShipNode;
import sg.spring.seabattle2.persistence.ShipPartRelation;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameMapPersistenceMapper implements IDomainPersistenceMapper<GameMap, GameMapNode> {
    public static final GameMapPersistenceMapper INSTANCE = new GameMapPersistenceMapper();

    private GameMapPersistenceMapper() {
    }


    @Override
    public @Nullable GameMap toDomain(@Nullable GameMapNode entity) {
        if (entity == null) {
            return null;
        }

        GameMap gameMap = new GameMap(entity.getRootSize());

        Map<UUID, Set<ShipPartRelation>> shipMap = entity.getShipParts().stream()
                .collect(Collectors.groupingBy(
                        relation -> relation.getShip().getIdentifier(),
                        Collectors.toSet()
                ));

        for (Map.Entry<UUID, Set<ShipPartRelation>> entry : shipMap.entrySet()) {
            for (ShipPartRelation shipPartRelation : entry.getValue()) {
                ShipPart shipPart = new ShipPart();
                shipPart.setIdentifier(entry.getKey());
                shipPart.setHit(shipPartRelation.isHit());
                shipPart.setElementId(shipPartRelation.getId());

                int currentIndex = shipPartRelation.getIndex();
                int[] otherIndexes = entry.getValue().stream()
                        .mapToInt(ShipPartRelation::getIndex)
                        .filter(index -> index != currentIndex)
                        .toArray();
                shipPart.setOtherPartsLocations(otherIndexes);

                gameMap.getShipParts()[currentIndex] = shipPart;
            }
        }

        return gameMap;
    }

    @Override
    public @Nullable GameMapNode toEntity(@Nullable GameMap domain) {
        if (domain == null) {
            return null;
        }

        GameMapNode gameMapNode = new GameMapNode();

        //TODO: fix naming
        gameMapNode.setRootSize(domain.getSizeRoot());

        for (int i = 0; i < domain.getShipParts().length; i++) {
            ShipPart part = domain.getShipParts()[i];

            if (part == null) {
                continue;
            }

            if (part == ShipPart.MISSED) {
                gameMapNode.getMissedIndexes().add(i);
            }

            ShipPartRelation relation = new ShipPartRelation();
            relation.setIndex(i);
            relation.setHit(part.isHit());
            relation.setId(part.getElementId());

            ShipNode shipNode = new ShipNode();
            shipNode.setId(part.getIdentifier());

            relation.setShip(shipNode);
            gameMapNode.getShipParts().add(relation);
        }

        return gameMapNode;
    }
}
