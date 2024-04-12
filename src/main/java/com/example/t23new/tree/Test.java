package com.example.t23new.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static void main(String[] args) {
        for (int i = 500; i <= 5000; i+=500) {
            test(i);
        }
    }

    public static void test(int n) {
        TwoThreeTreeTest<Integer, Integer> tree = new TwoThreeTreeTest<>(Integer.class);
        List<Integer> keys = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            var e = random.nextInt();
            if (keys.contains(e)) {
                i--;
                continue;
            }
            keys.add(e);
            tree.insert(e, e, () -> {
            });
        }
        AtomicInteger insert = new AtomicInteger();
        AtomicInteger delete = new AtomicInteger();
        AtomicInteger search = new AtomicInteger();

        for (int i = 0; i < n / 2; i++) {
            if (i % 10 == 0) {
                tree.remove(random.nextInt(), delete::incrementAndGet);
                tree.insert(keys.get(random.nextInt(keys.size())), 0, insert::incrementAndGet);
                try {
                    tree.get(random.nextInt(), search::incrementAndGet);
                } catch (Exception ignored) {
                }

            } else {
                int randI = random.nextInt(keys.size());
                tree.remove(keys.get(randI), delete::incrementAndGet);
                int key = random.nextInt();
                tree.insert(key, 0, insert::incrementAndGet);
                keys.set(randI, key);
                try {
                    tree.get(keys.get(random.nextInt(keys.size())), search::incrementAndGet);
                } catch (Exception ignored) {
                }

            }
        }
        System.out.println(tree.size() + ";" +
                Math.log(n) / Math.log(3) + ";" +
                Math.log(n) / Math.log(2) + ";" +
                insert.doubleValue() / ((double) n / 2) + ";" +
                delete.doubleValue() / ((double) n / 2) + ";" +
                search.doubleValue() / ((double) n / 2)
        );

    }
}
