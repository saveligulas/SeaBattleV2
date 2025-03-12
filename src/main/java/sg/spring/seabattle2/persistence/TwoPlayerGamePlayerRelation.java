package sg.spring.seabattle2.persistence;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerColor;

@RelationshipProperties
@Getter
@Setter
public class TwoPlayerGamePlayerRelation {
    @RelationshipId
    private String id;
    @Enumerated(value = EnumType.STRING)
    private TwoPlayerColor color;
    @TargetNode
    private TwoPlayerGamePlayerNode player;
}
