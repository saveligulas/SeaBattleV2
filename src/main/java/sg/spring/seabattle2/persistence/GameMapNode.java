package sg.spring.seabattle2.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import sg.spring.core.persistence.provider.neo4j.Neo4jSerialIdentifier;

import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
public class GameMapNode extends Neo4jSerialIdentifier {
    @Relationship(type = ShipPartRelation.TYPE, direction = Relationship.Direction.OUTGOING)
    private Set<ShipPartRelation> shipParts = new HashSet<>();
    private Integer rootSize;
    private Set<Integer> missedIndexes = new HashSet<>();
}
