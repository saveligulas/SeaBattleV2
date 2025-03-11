package sg.spring.seabattle2.persistence.mapper;

import sg.spring.core.mapper.IDomainPersistenceMapper;
import sg.spring.seabattle2.domain.twoplayer.TwoPlayerGame;
import sg.spring.seabattle2.persistence.TwoPlayerGameNode;

public class TwoPlayerGamePersistenceMapper implements IDomainPersistenceMapper<TwoPlayerGame, TwoPlayerGameNode> {
    public static final TwoPlayerGamePersistenceMapper INSTANCE = new TwoPlayerGamePersistenceMapper();

    private TwoPlayerGamePersistenceMapper() {
    }


    @Override
    public TwoPlayerGame toDomain(TwoPlayerGameNode entity) {
        if (entity == null) {
            return null;
        }

        TwoPlayerGame game = new TwoPlayerGame(entity.getIdentifier());
        game.setPlayerRed(TwoPlayerGamePlayerPersistenceMapper.INSTANCE.toDomain(entity.getPlayerRed()));
        game.setPlayerBlue(TwoPlayerGamePlayerPersistenceMapper.INSTANCE.toDomain(entity.getPlayerBlue()));
        game.setAllowedShips(entity.getAllowedShips());
        game.setGamePhase(entity.getPhase());

        return game;
    }

    @Override
    public TwoPlayerGameNode toEntity(TwoPlayerGame domain) {
        if (domain == null) {
            return null;
        }

        TwoPlayerGameNode entity = new TwoPlayerGameNode();
        entity.setId(domain.getUuid());
        entity.setPlayerRed(TwoPlayerGamePlayerPersistenceMapper.INSTANCE.toEntity(domain.getPlayerRed()));
        entity.setPlayerBlue(TwoPlayerGamePlayerPersistenceMapper.INSTANCE.toEntity(domain.getPlayerBlue()));
        entity.setAllowedShips(domain.getAllowedShips());
        entity.setPhase(domain.getGamePhase());

        return entity;
    }
}
