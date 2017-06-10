package nl.yoshuan.pricecomparer.shared;

import java.util.List;

public abstract class DataScraper<E> {

    public abstract List<E> getAllProducts();

    protected void waitBetweenRequests(long durationInMillis) {
        try {
            Thread.sleep(durationInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
