package sg.spring.seabattle2.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Getter
@Setter
public class GameMapNodeRelation {
    @RelationshipId
    private Long id;
    @TargetNode
    private GameMapNode map;
}
