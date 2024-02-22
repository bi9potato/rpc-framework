package com.renghe.remoting.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public void start(int port) {

        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {

            ThreadFactory threadFactory = Executors.defaultThreadFactory();
            ExecutorService threadPool = new ThreadPoolExecutor(10, 100, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100), threadFactory);

            while (true) {

                Socket socket = serverSocket.accept();
                logger.info("client connected");

                threadPool.execute( ()-> {
                    handleClient(socket);
                });

            }

        } catch (Exception e) {
//            System.out.printf(e.getMessage());
            logger.error("error: ", e.toString());
        }


    }

    private void handleClient(Socket socket) {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
        ) {

            Message message = (Message) objectInputStream.readObject();
            logger.info("server receive message:" + message.getContent());

            message.setContent("server response");
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();

        } catch (Exception e) {
//            System.out.println(e.getMessage());
            logger.error("error: ", e.toString());
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(6666);

    }

}
