package ru.gbmodule3db.dbinchat;

import lesson3FileIO.FileClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;


public class ChatClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private boolean nickIsInitialisation = false;

    private final Controller controller;

    public ChatClient(Controller controller) {
        this.controller = controller;
        //openConnection(); // удалили этот метод из этого конструктора в конструктор controller()
    }

    public void openConnection() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    //long start = System.currentTimeMillis();
                    //long end = start + 10*1000; // 60 seconds * 1000 ms/sec
                    // System.currentTimeMillis() < end
                    final String nick;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long start = System.currentTimeMillis();
                            long end = start + 120*1000; // 60 seconds * 1000 ms/sec
                            while (System.currentTimeMillis() < end) {
                            }
                            //break;
                            if (nickIsInitialisation == false) {
                                //controller.setAuth(false);
                                closeConnection();
                            }
                            //controller.setAuth(false);
                        }
                    });
                    thread.start();
                    while (true) {  // while (true)
                        final String msgAuth = in.readUTF();
                        if (msgAuth.startsWith("/authok")) {
                            final String[] split = msgAuth.split(" ");
                            //final String nick = split[1];
                            nick = split[1];
                            nickIsInitialisation = true;
                            controller.assigningUserNickToForm(nick); // выводим в спец. поле имя клиента
                            ////////////////  здесь должен быть метод, который выводит историю чата !!!!!!!!!!!!!!!!
                            FileClient f = new FileClient();
                            controller.addMessage(f.outputHistoryOfChat(nick, 100));  // метод, который выводит историю чата
                            controller.addMessage("Успешная авторизация под ником " + nick);
                            controller.setAuth(true);
                            break;
                        }
                    }
                    while (true) {
                        final String message = in.readUTF();
                        System.out.println("Receive message: " + message);
                        if (message.startsWith("/")) {
                            if ("/end".equals(message)) {
                                controller.setAuth(false);
                                break;
                            }
                            if (message.startsWith("/clients")) { // /clients nick1 nick0 nick3 ...
                                //final  String[] tokens = message.replace("/clients ", "").split(" "); // удаляем
                                // нулевой элемент /clients из массива и сплитуем оставшийся массив
                                final  String[] tokens = message.replace("/clients ", "").replace(nick, "").split(" ");
                                 final List<String> clients = Arrays.asList(tokens);
                                 controller.updateClientList(clients);
                            }
                        }
                        controller.addMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() { // private
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public void sendMessage(String message) {
        try {
            System.out.println("Send message: " + message);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
