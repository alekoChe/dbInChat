package server;

import db.DataBase;

import java.sql.SQLException;

public class ServerRunner {
    public static void main(String[] args) {

        new ChatServer().run();

//        DataBase db = new DataBase();
//        try {
//            db.connect();   // когда приложение стартует
//            db.createTable();
//            db.insert("Иванов", "Иван", "ivan1", "login1","pass1");
//            db.insert("Петров", "Петр", "pedro1", "login2","pass2");
//            db.insert("Иванова", "Мария", "masha1", "login3","pass3");
//            db.insert("Сидоров", "Иван", "ivan2", "login4","pass4");
//            db.readEx();
//            db.dropTable();
//
////            jdbcApp.update(1, 100);
////            jdbcApp.select(1);
////            jdbcApp.select(2);
////            jdbcApp.selectByName("bob%' union SELECT 1, sql, 1 FROM sqlite_master --");
////            jdbcApp.batchInsert();
////            jdbcApp.selectByName("Bob%");
////            jdbcApp.dropTable();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            db.disconnect();  // когда приложение заканчивает работу
//        }
    }
}
