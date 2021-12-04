package server;

import lesson3FileIO.FileClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private final AuthService authService;  // сервис аутентификации
    private final Map<String, ClientHandler> clients; // список клиентов

    public ChatServer() {
        this.authService = new SimpleAuthService();
        this.clients = new HashMap<>();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                System.out.println("Wait client connection...");
                final Socket socket = serverSocket.accept(); // для каждого клиента получаем новый сокет
                new ClientHandler(socket, this);  // передаем новому клиенту новый сокет и класс ChatServer
                System.out.println("Client connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);
    }

    public void subscribe(ClientHandler client) { // при подключении клиента
        clients.put(client.getNick(), client);  // добавляем клиента в мапу подключенных клиентов
        broadcastClientsList();  // и рассылаем сообщения другим клиентам
    }

    public void unsubscribe(ClientHandler client) { // при отключении клиента
        clients.remove(client.getNick());  // удаляем клиента из списка подключенных клиентов
        broadcastClientsList();   // и рассылаем сообщения другим клиентам
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String msg) {
        final ClientHandler client = clients.get(nickTo);
        if (client != null) {
            client.sendMessage("От " + from.getNick() + ": " + msg);
            from.sendMessage("Участнику " + nickTo + ": " + msg);
            /////////// запись в файл allHistory                //////////////////////////////////////
            final String mess ="От " + from.getNick() + " кому " + nickTo + ": " + msg;           ////
            FileClient f = new FileClient();                                                      ////
            //f.writeInfoIntoFile(mess, "allHistory.txt");                                     ////
            /////////// запись в файл отправителя ////////////////////////////////////////////////////
            final  String pathSendersFile = "history_" + from.getNick() + ".txt";                 ////
            f.writeInfoIntoFile(mess, pathSendersFile);                                           ////
            //////////// запись в файл адресата //////////////////////////////////////////////////////
            final String pathAddresseesFile = "history_" + nickTo + ".txt";                       ////
            f.writeInfoIntoFile(mess, pathAddresseesFile);

            return;
        }
        from.sendMessage("Участника с ником " + nickTo + " нет в чат-комнате" );
    }

    public void broadcastClientsList() {
        StringBuilder clientsCommand = new StringBuilder("/clients ");
        for (ClientHandler client : clients.values()) {
            clientsCommand.append(client.getNick()).append(" ");
        }
        broadcast(clientsCommand.toString());
    }

    public void broadcast(String msg) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(msg);
        }
    }
}
