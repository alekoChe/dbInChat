package db;

import server.SimpleAuthService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private Connection connection;
    private Statement statement;  // этот класс позволяет делать запросы к базе данных

    public DataBase(){
//        try {
//            //this.dropTable();
//            this.connect();   // когда приложение стартует
//            this.createTable();
//            this.insert("Сидоровa", "Мария", "nick0", "login0","pass0");
//            this.insert("Иванов", "Иван", "nick1", "login1","pass1");
//            this.insert("Петров", "Петр", "nick2", "login2","pass2");
//            this.insert("Иванова", "Мария", "nick3", "login3","pass3");
//            this.insert("Сидоров", "Иван", "nick4", "login4","pass4");
//            this.insert("Саидов", "Саид", "nick5", "login5","pass5");
//            this.insert("Николаев", "Николай", "nick6", "login6","pass6");
//            this.insert("Николаева", "Анна", "nick7", "login7","pass7");
//            //this.readEx();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            //this.disconnect();  // когда приложение заканчивает работу
//        }
    }

    public static void main(String[] args) {
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
            db.select(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws SQLException {  // производим подключение к базе данных
        connection = DriverManager.getConnection("jdbc:sqlite:chatClients.db"); // в кавычках строка подключения
        connection.setAutoCommit(false);
        statement = connection.createStatement(); // активизация стейтмента
    }

    public void createTable() throws SQLException {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS chatClients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "lastname TEXT," +
                    "firstname TEXT," +
                    "nick TEXT," +
                    "login TEXT," +
                    "password TEXT" +
                    ");");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connection.rollback();
        }
    }
    public void insert(final String lastname, final String firstname, final String nick,
                       final String login, final String password) throws SQLException {
//        statement.executeUpdate("INSERT INTO students(name, score) VALUES (" + name + ", " + score + ")"); // sql injection!!!
        try (final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chatClients(lastname, " +
                "firstname, nick, login, password) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, lastname);
            preparedStatement.setString(2, firstname);
            preparedStatement.setString(3, nick);
            preparedStatement.setString(4, login);
            preparedStatement.setString(5, password);
            preparedStatement.executeUpdate();
        }
    }
    public void readEx() throws SQLException {
        try (ResultSet rs = statement.executeQuery("SELECT * FROM chatClients where id = 1;")) {
            while (rs.next()) {
                System.out.println(rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6));
            }
        }
    }

    public void writeFromDBIntoUsersList(List users, Integer id) throws SQLException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chatClients WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                //System.out.printf("%s - %s - %s\n", resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
                users.add(resultSet.getString(4) + resultSet.getString(5) + resultSet.getString(6));
            }
        }
    }
    public void select(Integer id) throws SQLException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chatClients WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.printf("%s - %s - %s\n", resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
            }
        }
    }
    public String selectNick(Integer id) throws SQLException {
        String nick = "";
        try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chatClients WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                nick = resultSet.getString(4);
            }
        }
        return nick;
    }
    public String selectPass(Integer id) throws SQLException {
        String pass = "";
        try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chatClients WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pass = resultSet.getString(6);
            }
        }
        return pass;
    }
    public String selectLogin(Integer id) throws SQLException {
        String login = "";
        try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chatClients WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                login = resultSet.getString(5);
            }
        }
        return login;
    }
    public void dropTable() throws SQLException {
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS chatClients");
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connection.rollback();
        }
    }

}
