package db;

import java.sql.*;

public class DataBase {
    private Connection connection;
    private Statement statement;  // этот класс позволяет делать запросы к базе данных

    public static void main(String[] args) {
        DataBase db = new DataBase();
        try {
            db.connect();   // когда приложение стартует
            db.createTable();
            db.insert("Иванов", "Иван", "ivan1", "login1","pass1");
            db.insert("Петров", "Петр", "pedro1", "login2","pass2");
            db.insert("Иванова", "Мария", "masha1", "login3","pass3");
            db.insert("Сидоров", "Иван", "ivan2", "login4","pass4");
            db.readEx();
            db.dropTable();

//            jdbcApp.update(1, 100);
//            jdbcApp.select(1);
//            jdbcApp.select(2);
//            jdbcApp.selectByName("bob%' union SELECT 1, sql, 1 FROM sqlite_master --");
//            jdbcApp.batchInsert();
//            jdbcApp.selectByName("Bob%");
//            jdbcApp.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();  // когда приложение заканчивает работу
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
    public void select(Integer id) throws SQLException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chatClients WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.printf("%s - %s - %s\n", resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
            }
        }
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
