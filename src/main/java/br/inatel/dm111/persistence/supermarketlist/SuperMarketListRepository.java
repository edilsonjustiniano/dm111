package br.inatel.dm111.persistence.supermarketlist;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface SuperMarketListRepository {

    void save(SuperMarketList superMarketList);

    List<SuperMarketList> findAll() throws ExecutionException, InterruptedException;

    Optional<SuperMarketList> findById(String id) throws ExecutionException, InterruptedException;

    void delete(String id) throws ExecutionException, InterruptedException;

    void update(SuperMarketList superMarketList);
}
