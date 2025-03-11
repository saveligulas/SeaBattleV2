package sg.spring.seabattle2.persistence;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Target;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import sg.spring.core.persistence.provider.neo4j.RelationshipPropertiesBase;

@RelationshipProperties
@Getter
@Setter
public class ShipPartRelation {
    public static final String TYPE = "HAS_HAPPENED";
    @Id
    private Integer index;
    private boolean isHit;
    @TargetNode
    private ShipNode ship;
}
