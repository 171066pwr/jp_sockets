package com.mycompany.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
public class RainGenerator implements Runnable {
    private final Random rand = new Random();
    private final Environment environment;
    private int average = 13;
    private long period = 5000;

    public RainGenerator(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run() {
        rand.setSeed(System.currentTimeMillis());
        while (!Thread.currentThread().isInterrupted()) {
            int roll = rand.nextInt(average * 8);
            roll = roll < average * 4 ? 0 : roll - average * 4;
            environment.setCurrentRainfall(roll);
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
