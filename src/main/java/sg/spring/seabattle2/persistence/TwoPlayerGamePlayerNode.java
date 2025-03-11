package sg.spring.seabattle2.persistence;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import sg.spring.core.persistence.provider.neo4j.Neo4jNodeIdentifier;
import sg.spring.core.persistence.provider.neo4j.Neo4jSerialIdentifier;
import sg.spring.seabattle2.domain.GameMap;
import sg.spring.seabattle2.domain.GamePhase;
import sg.spring.seabattle2.domain.GameState;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerColor;

@Node
@Getter
@Setter
public class TwoPlayerGamePlayerNode extends Neo4jSerialIdentifier {
    @Enumerated(value = EnumType.STRING)
    private TwoPlayerColor color;
    private String name;
    @Relationship(type = "HAS_MAP", direction = Relationship.Direction.OUTGOING)
    private GameMapNode gameMap;
}
