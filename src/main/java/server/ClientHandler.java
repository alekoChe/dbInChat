package server;

import lesson3FileIO.FileClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHandler {
    private static final String COMMAND_PREFIX = "/";
    private static final String SEND_MESSAGE_TO_CLIENT_COMMAND = COMMAND_PREFIX + "w";
    private static final String END_COMMAND = COMMAND_PREFIX + "end";

    private final Socket socket;
    private final ChatServer server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nick;
    private final ExecutorService cachedService;

    public ClientHandler(Socket socket, ChatServer server) {
        try {
            this.nick = "";
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.cachedService = Executors.newCachedThreadPool();

            cachedService.execute(() -> {
                new Thread(() -> { // слушает сообщения от клиентов и читает их
                    try {
                        authenticate();  // перед тем как писать сообщения пользователь должен авторизоваться
                        readMessages();
                    } finally {
                        closeConnection();
                    }
                }).start();
            });
            cachedService.shutdown();

//            new Thread(() -> { // слушает сообщения от клиентов и читает их
//                try {
//                    authenticate();  // перед тем как писать сообщения пользователь должен авторизоваться
//                    readMessages();
//                } finally {
//                    closeConnection();
//                }
//            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void closeConnection() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                server.unsubscribe(this);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticate() {
        while (true) {  // true
            try {
                final String str = in.readUTF();
                if (str.startsWith("/auth")) {   // auth + login0 + pass0
                    final String[] split = str.split(" ");
                    final String login = split[1];
                    final String password = split[2];
                    final String nick = server.getAuthService().getNickByLoginAndPassword(login, password);
                    if (nick != null) {
                        if (server.isNickBusy(nick)) {
                            sendMessage("Пользователь уже авторизован");
                            continue;
                        }
                        sendMessage("/authok " + nick);
                        this.nick = nick;
                        server.broadcast("Пользователь " + nick + " зашел в чат");
                        server.subscribe(this);
                        break;
                    } else {
                        sendMessage("Неверные логин и пароль");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendMessage(String message) {
        try {
            System.out.println("SERVER: Send message to " + nick);
            out.writeUTF(message);
            FileClient f = new FileClient();
            final String path = "history_" + nick + ".txt";
            f.writeInfoIntoFile(message, path); // запись сообщения в историю
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() {
        try {
            while (true) {
                final String msg = in.readUTF();
                System.out.println("Receive message: " + msg);
                if (msg.startsWith(COMMAND_PREFIX)) {
                    if (END_COMMAND.equals(msg)) {
                        break;
                    }
                    if (msg.startsWith(SEND_MESSAGE_TO_CLIENT_COMMAND)) { // /w nick1 kjhgafljgh
                        final String[] token = msg.split(" ");
                        final String nick = token[1];
                        final int index = 2 + token[0].length() + token[1].length();
                        server.sendMessageToClient(this, nick, msg.substring(index)); // отправка тела сообщения
                    }
                    continue;
                }
                server.broadcast(nick + ": " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}
