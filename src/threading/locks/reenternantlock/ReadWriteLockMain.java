package threading.locks.reenternantlock;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockMain {
    public static final int HIGHEST_PRICE = 1000;
    public static void main(String[] args) throws InterruptedException {
        InventoryDataBase inventoryDataBase = new InventoryDataBase();
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            inventoryDataBase.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writer = new Thread(() -> {
            while (true) {
                inventoryDataBase.addItem(random.nextInt(HIGHEST_PRICE));
                inventoryDataBase.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {}

            }
        });
        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();
        for (int readerIndex = 0; readerIndex < numberOfReaderThreads; readerIndex++) {
            Thread reader = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    int upperBound = random.nextInt(HIGHEST_PRICE);
                    int lowerBound = upperBound > 0 ? random.nextInt(upperBound) : 0;
                    inventoryDataBase.getNumberOfItemsInPriceRange(lowerBound, upperBound);
                }
            });
            reader.setDaemon(true);
            readers.add(reader);
        }

        long startReadingTime = System.currentTimeMillis();
        for (Thread reader: readers) {
            reader.start();
        }

        for (Thread reader: readers) {
            reader.join();
        }
        long endReadingTime = System.currentTimeMillis();
        System.out.println(String.format("Reading took %d ms", endReadingTime - startReadingTime));
    }

    public static class InventoryDataBase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = reentrantReadWriteLock.readLock();
        private Lock writeLock = reentrantReadWriteLock.writeLock();
        private ReentrantLock lock = new ReentrantLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
            readLock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);
                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);
                int sum = 0;
                for (int numberOfItems : rangeOfPrices.values()) {
                    sum += numberOfItems;
                }
                return sum;
            } finally {
                readLock.unlock();
            }
        }

        public void addItem(int price) {
            writeLock.lock();
            try {
                Integer numberOfItems = priceToCountMap.getOrDefault(price, 0) + 1;
                priceToCountMap.put(price, numberOfItems);
            } finally {
                writeLock.unlock();
            }

        }

        public void removeItem(int price) {
            writeLock.lock();
            try {
                Integer numberOfItems = priceToCountMap.getOrDefault(price, 1) - 1;
                if (numberOfItems == 0) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItems);
                }
            } finally {
                writeLock.unlock();
            }
        }
    }
}
