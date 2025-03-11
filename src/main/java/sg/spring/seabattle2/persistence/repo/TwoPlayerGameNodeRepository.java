package sg.spring.seabattle2.persistence.repo;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import sg.spring.seabattle2.persistence.TwoPlayerGameNode;

import java.util.UUID;

@Repository
public interface TwoPlayerGameNodeRepository extends Neo4jRepository<TwoPlayerGameNode, UUID> {
}
