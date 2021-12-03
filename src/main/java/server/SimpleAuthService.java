package server;

import db.DataBase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {
    private final List<UserData> users; // <UserData>

    public SimpleAuthService() {
        users = new ArrayList<>();  // инициализируем users, иначе ничего туда не положишь
//        for (int i = 0; i < 5; i++) {
//            users.add(new UserData("login" + i, "pass" + i, "nick" + i));
//            FileClient f = new FileClient(); // создаем объект класса FileClient
//            f.createUserHistoryFileInForLoop(i); // и применяем метод createUserHistoryFileInForLoop(i)
        
        DataBase db = new DataBase();
        try {
            db.connect();
            db.dropTable();
            db.createTable();
            db.insert("Сидоровa", "Мария", "nick0", "login0","pass0");
            db.insert("Иванов", "Иван", "nick1", "login1","pass1");
            db.insert("Петров", "Петр", "nick2", "login2","pass2");
            db.insert("Иванова", "Мария", "nick3", "login3","pass3");
            db.insert("Сидоров", "Иван", "nick4", "login4","pass4");
            db.insert("Саидов", "Саид", "nick5", "login5","pass5");
            db.insert("Николаев", "Николай", "nick6", "login6","pass6");
            db.insert("Николаева", "Анна", "nick7", "login7","pass7");

            for (int i = 0; i < 8; i++) {
                users.add(new UserData(db.selectLogin(i + 1), db.selectPass(i + 1), db.selectNick(i + 1)));
            }
            db.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.nick;
            }
        }
        return null;
    }

    private static class UserData { // класс для хранения данных о пользователе
        private final String login;
        private final String password;
        private final String nick;

        public UserData(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }
}
