
package com.epam.java.command;

import com.epam.java.util.Logging;

public class DeadLock implements Command {

    private static final Logging.Logger LOGGER = Logging.getLogger();

    private final Object monitor1 = new Object();
    private final Object monitor2 = new Object();

    @Override
    public void start(String[] args) throws Exception {
        final Thread thread1 = new Thread(() -> {
            synchronized (monitor1) {
                LOGGER.info("Acquired monitor1 by thread 1");
                sleep();
                synchronized (monitor2) {
                    LOGGER.info("Acquired monitor2 by thread 1");
                    sleep();
                }
            }
        });
        final Thread thread2 = new Thread(() -> {
            synchronized (monitor2) {
                LOGGER.info("Acquired monitor2 by thread 2");
                sleep();
                synchronized (monitor1) {
                    LOGGER.info("Acquired monitor1 by thread 2");
                    sleep();
                }
            }
        });
        LOGGER.info("Starting threads");
        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void stop() throws Exception {
        //This method has been left empty intentionally as we do not require it for the given implementation.
    }
}
