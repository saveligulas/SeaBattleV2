package sg.spring.seabattle2.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import sg.spring.core.persistence.provider.neo4j.Neo4jNodeIdentifier;
import sg.spring.seabattle2.domain.GamePhase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Node
@Getter
@Setter
public class TwoPlayerGameNode extends Neo4jNodeIdentifier<UUID> {
    @Relationship(type = "HAS_PLAYER", direction = Relationship.Direction.OUTGOING)
    private Set<TwoPlayerGamePlayerRelation> players = new HashSet<>();
    private GamePhase phase;
    private List<Integer> allowedShips;

    public TwoPlayerGameNode() {
    }

    public TwoPlayerGameNode(UUID id) {
        super(id);
    }
}
