package ru.gbmodule3db.dbinchat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.List;

public class Controller {

    @FXML
    public ListView<String> clientList;
    @FXML
    public TextField clientNick;
    @FXML
    private HBox messageBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginField;
    @FXML
    private HBox loginBox;
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    private final ChatClient client;

    public Controller() {
        client = new ChatClient(this);
        client.openConnection();
    }

    public void btnSendClick(ActionEvent event) {
        final String message = textField.getText().trim();
        if (message.isEmpty()) {
            return;
        }
        client.sendMessage(message);
        textField.clear();
        textField.requestFocus();
    }

    public void addMessage(String message) {
        textArea.appendText(message + "\n");
    }

    public void btnAuthClick(ActionEvent actionEvent) {
        client.sendMessage("/auth " + loginField.getText() + " " + passwordField.getText());
    }

    public void setAuth(boolean isClientAuth) {
        loginBox.setVisible(!isClientAuth);
        messageBox.setVisible(isClientAuth);
        //textArea.setVisible(isClientAuth); // эта строка теперь не нужна
    }

    public void selectClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) { //два раза кликаем мышкой
            final String message = textField.getText(); // берем сообщение
            final String nick = clientList.getSelectionModel().getSelectedItem(); // достаем ник
            textField.setText("/w " + nick + " " + message);  // и все это отправляем участникам
            textField.requestFocus();
            textField.selectEnd(); // перевод курсора в конец
        }
    }

    public void updateClientList(List<String> clients) {
        clientList.getItems().clear();
        clientList.getItems().addAll(clients);
    }

    public void assigningUserNickToForm(String nick) {
        clientNick.setText("Мы рады что вы с нами, " + nick + "!");
    }
}