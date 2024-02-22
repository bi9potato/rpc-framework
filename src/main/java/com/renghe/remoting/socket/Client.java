package com.renghe.remoting.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    public Object send(Message message, String host, int port) {

        try (Socket socket = new Socket(host, port)) {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return null;

    }

    public static void main(String[] args) {
        Client client = new Client();
        Message message = (Message) client.send(new Message("client content"), "127.0.0.1", 6666);
        System.out.println(message.getContent());
    }

}
