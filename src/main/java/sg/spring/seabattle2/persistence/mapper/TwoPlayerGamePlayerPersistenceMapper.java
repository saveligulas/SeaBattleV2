package sg.spring.seabattle2.persistence.mapper;

import org.jspecify.annotations.Nullable;
import sg.spring.core.mapper.IDomainPersistenceMapper;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGame;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGamePlayer;
import sg.spring.seabattle2.persistence.TwoPlayerGamePlayerNode;

public class TwoPlayerGamePlayerPersistenceMapper implements IDomainPersistenceMapper<TwoPlayerGamePlayer, TwoPlayerGamePlayerNode> {
    public static final TwoPlayerGamePlayerPersistenceMapper INSTANCE = new TwoPlayerGamePlayerPersistenceMapper();
    
    private TwoPlayerGamePlayerPersistenceMapper() {
    }
    
    @Override
    public @Nullable TwoPlayerGamePlayer toDomain(@Nullable TwoPlayerGamePlayerNode entity) {
        if (entity == null) {
            return null;
        }

        TwoPlayerGamePlayer player = new TwoPlayerGamePlayer();

        player.setIdentifier(entity.getIdentifier());
        player.setColor(entity.getColor());
        player.setName(entity.getName());
        player.setOpponentMap(GameMapPersistenceMapper.INSTANCE.toDomain(entity.getGameMap()));

        return player;
    }

    @Override
    public @Nullable TwoPlayerGamePlayerNode toEntity(@Nullable TwoPlayerGamePlayer domain) {

        if (domain == null) {
            return null;
        }

        TwoPlayerGamePlayerNode playerNode = new TwoPlayerGamePlayerNode();
        playerNode.setId(domain.getIdentifier());
        playerNode.setColor(domain.getColor());
        playerNode.setName(domain.getName());
        playerNode.setGameMap(GameMapPersistenceMapper.INSTANCE.toEntity(domain.getOpponentMap()));

        return playerNode;
    }
}
