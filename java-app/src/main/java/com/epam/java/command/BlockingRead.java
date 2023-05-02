
package com.epam.java.command;

import com.epam.java.util.Logging;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class BlockingRead implements Command {

    private static final Logger LOGGER = Logging.getLogger();
    private static final int DEFAULT_PORT = 2789;
    private ServerSocket serverSocket;

    @Override
    public void start(String[] args) throws Exception {
        final int port = port(args);
        serverSocket = new ServerSocket(port);

        // ifdown during read, see what happens
        while (!Thread.currentThread().isInterrupted()) {
            LOGGER.info("Waiting for connection on port " + port);
            final Socket sock = serverSocket.accept();
            LOGGER.info(String.format("Connected, will echo all requests on port %d", sock.getPort()));
            final InputStream inputStream = sock.getInputStream();

            int dataByte;
            while ((dataByte = inputStream.read()) != -1) {
                LOGGER.info(Character.toString((char) dataByte));
            }
            LOGGER.info("Disconnected");
        }
    }

    private int port(String[] args) {
        if (args.length < 2) {
            return DEFAULT_PORT;
        }
        return Integer.parseInt(args[1]);
    }

    @Override
    public void stop() throws Exception {
        Thread.currentThread().interrupt();
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
