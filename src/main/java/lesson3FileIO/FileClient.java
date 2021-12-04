package lesson3FileIO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileClient {
    private final String path = "C:\\Users\\aleko\\IdeaProject\\dbInChat\\";

    public String getPath() {
        return path;
    }

    public static void main(String[] args) {
//        FileClient f = new FileClient();
//        File file = new File("C:\\Users\\aleko\\IdeaProject\\dbInChat", "myFile_01.txt");
//        boolean exists = file.exists();
//        System.out.println(exists);
//
//        File outputFile = new File("history_nick4.txt");
//        String s = "Java hello!";
//        f.writeInfoIntoFile(s, "history_nick4.txt");
//
//        //////////////////////////////////////////////
//        String history = f.outputHistoryOfChat("nick0", 100);
//        System.out.println(history);
    }
    public void createUserHistoryFileInForLoop(int i) {
        /** метод создает фаил (в котором должна храниться история клиента) в цикле for
         * В качестве аргумента: i- итератор цикла.
         */
        FileClient f = new FileClient();
        String filename;
        filename = f.getPath() + "history_nick" + i + ".txt";
        File file1 = new File(filename);
        if (!file1.exists()) {
            try {
                Files.createFile(Path.of(String.valueOf(file1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeInfoIntoFile(String info, String path){
        /** Записываем инфу в файл. На входе информация info(С переводом строки) и путь к файлу path
         *
          */
        //final String path = "history_" + nick + ".txt";
        File outputFile = new File(path);
        info += "\n";
        final byte[] bytes = info.getBytes(StandardCharsets.UTF_8);
//        try(FileOutputStream fos = new FileOutputStream(outputFile, true)) {
//            fos.write(bytes);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try(FileOutputStream fos = new FileOutputStream(outputFile, true);
            final BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String outputHistoryOfChat(String nick, int lengthOutList) {
        /** выводим историю чата для клиента. В качестве параметров ник клиента,
         * (этот ник будет преобразован в название файла), и размер выводимого списка - lengthOutList(по условию 100)
         */
        FileClient f = new FileClient();
        Path path = Path.of( f.getPath() + "history_" + nick + ".txt");
        List<String> list = null;
        try {
            list = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int startList = 0; // начало выводимого списка
        if (list.size() > lengthOutList) {
            startList = list.size() - lengthOutList;
        }
        String historyChat = "";
        System.out.println(list.size());
        for (int i = startList; i < list.size(); i++) {
            historyChat += list.get(i) + "\n";
            //System.out.println(list.get(i));
        }
        //System.out.println(historyChat);
        return historyChat;
    }
}
