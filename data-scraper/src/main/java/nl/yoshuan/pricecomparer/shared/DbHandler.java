package nl.yoshuan.pricecomparer.shared;

import java.util.ArrayList;
import java.util.List;

public abstract class DbHandler<E> {

    public abstract void persistEntitiesToDb(List<E> products);

    protected List<DbEntitiesHolder> toDbEntityHolderList(List<E> products) {
        List<DbEntitiesHolder> dbEntitiesHolderList = new ArrayList<>();

        for (E product : products) {
            dbEntitiesHolderList.add(mapToDbEntities(product));
        }

        return dbEntitiesHolderList;
    }

    protected abstract DbEntitiesHolder mapToDbEntities(E product);

}
