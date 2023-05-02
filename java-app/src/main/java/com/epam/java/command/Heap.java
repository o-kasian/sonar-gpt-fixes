
package com.epam.java.command;

import com.epam.java.util.Logging;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Heap implements Command {

    private static final Logging.Logger LOGGER = Logging.getLogger();

    private static final String DEFAULT_MODE = "normal";
    private static final Map<String, Command> MODES = new HashMap<>();

    static {
        MODES.put("global", new GlobalOOM());
        MODES.put("local", new LocalOOMCommand());
        MODES.put("soft", new SoftOOM());
    }

    private Command cmd;

    @Override
    public void start(String[] args) throws Exception {
        final String id = args.length > 1 ? args[1] : DEFAULT_MODE;
        cmd = MODES.get(id);
        cmd.start(args);
    }

    @Override
    public void stop() throws Exception {
        if (cmd != null) {
            cmd.stop();
        }
    }

    private static class LocalOOMCommand implements Command {

        private final Object monitor = new Object();
        private final Random random = new Random();

        @Override
        public void start(String[] args) throws Exception {
            try {
                final LinkedList<byte[]> list = new LinkedList<>();
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] bytes = new byte[1024 * 1024];
                    random.nextBytes(bytes);
                    list.add(bytes);
                    synchronized (monitor) {
                        while (list.size() > 50) { // added while loop
                            monitor.wait(100);
                        }
                    }
                }
            } catch (InterruptedException interruptedException) { // catching specific exception
                LOGGER.error(interruptedException);
                Thread.currentThread().interrupt(); // re-interrupting the thread
            } catch (Exception exception) { // catching specific exception
                LOGGER.error(exception); // logging the exception
            }
        }

        @Override
        public void stop() throws Exception {
            Thread.currentThread().interrupt();
            synchronized (monitor) {
                monitor.notifyAll();
            }
        }
    }

    private static class GlobalOOM implements Command {

        private final LinkedList<byte[]> list = new LinkedList<>();
        private final Object monitor = new Object();
        private final Random random = new Random();

        @Override
        public void start(String[] args) throws Exception {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] bytes = new byte[1024 * 1024];
                    random.nextBytes(bytes);
                    list.add(bytes);
                    synchronized (monitor) {
                        while (list.size() > 50) { // added while loop
                            monitor.wait(100);
                        }
                    }
                }
            } catch (InterruptedException interruptedException) { // catching specific exception
                LOGGER.error(interruptedException);
                Thread.currentThread().interrupt(); // re-interrupting the thread
            } catch (Exception exception) { // catching specific exception
                LOGGER.error(exception); // logging the exception
            }
        }

        @Override
        public void stop() throws Exception {
            Thread.currentThread().interrupt(); // re-interrupting the thread
            synchronized (monitor) {
                monitor.notifyAll();
            }
        }
    }

    private static class SoftOOM implements Command {

        private final LinkedList<SoftReference<byte[]>> list = new LinkedList<>();
        private final Object monitor = new Object();
        private final Random random = new Random();

        @Override
        public void start(String[] args) throws Exception {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] bytes = new byte[1024 * 1024];
                    random.nextBytes(bytes);
                    list.add(new SoftReference<>(bytes));
                    synchronized (monitor) {
                        while (list.size() > 50) { // added while loop
                            monitor.wait(100);
                        }
                    }
                }
            } catch (InterruptedException interruptedException) { // catching specific exception
                LOGGER.error(interruptedException);
                Thread.currentThread().interrupt(); // re-interrupting the thread
            } catch (Exception exception) { // catching specific exception
                LOGGER.error(exception); // logging the exception
            }
        }

        @Override
        public void stop() throws Exception {
            Thread.currentThread().interrupt(); // re-interrupting the thread
            synchronized (monitor) {
                monitor.notifyAll();
            }
        }
    }
}
