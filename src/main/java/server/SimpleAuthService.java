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
//        }
        DataBase db = new DataBase();
        for (int i = 0; i < 5; i++) {
            try {
                db.writeFromDBIntoUsersList(users, i + 1);
                db.dropTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        try {
//            db.writeFromDBIntoUsersList(users);
//            db.dropTable();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
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
