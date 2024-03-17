package br.inatel.dm111.persistence.supermarketlist;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//@Component
public class SuperMarketListMemoryRepository implements SuperMarketListRepository{

    private Set<SuperMarketList> db = new HashSet<>();

    @Override
    public void save(SuperMarketList superMarketList) {
        db.add(superMarketList);
    }

    @Override
    public List<SuperMarketList> findAll() {
        return db.stream().toList();
    }

    @Override
    public Optional<SuperMarketList> findById(String id) {
        return db.stream()
                .filter(spl -> spl.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(String id) {
        db.removeIf(spl -> spl.getId().equals(id));
    }

    @Override
    public void update(SuperMarketList superMarketList) {
        delete(superMarketList.getId());
        save(superMarketList);
    }
}
