package br.inatel.dm111.persistence.supermarketlist;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class SuperMarketListFirebaseRepository implements SuperMarketListRepository{

    private static final String COLLECTION_NAME = "supermarket_list";

    private final Firestore firestore;

    public SuperMarketListFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void save(SuperMarketList superMarketList) {
        firestore.collection(COLLECTION_NAME)
                .document(superMarketList.getId())
                .set(superMarketList);
    }

    @Override
    public List<SuperMarketList> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(spl -> spl.toObject(SuperMarketList.class))
                .toList();
    }

    @Override
    public Optional<SuperMarketList> findById(String id) throws ExecutionException, InterruptedException {
        var spl = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(SuperMarketList.class);

        return Optional.ofNullable(spl);
    }

    @Override
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    @Override
    public void update(SuperMarketList superMarketList) {
        save(superMarketList);
    }
}
