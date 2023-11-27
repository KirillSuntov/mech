import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.junit.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;


public class JavaTelegramBot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        String botName = "https://t.me/mechta_deshman_bot"; // В место звездочек указываем имя созданного вами ранее Бота
        String botToken = "6111936818:AAEYx_2p0MpQTvwfpCwPx291x0IhfMzbL3U"; // В место звездочек указываем токен созданного вами ранее Бота
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new JavaTelegramBot(botName, botToken));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    final String botName;
    final String botToken;

    public JavaTelegramBot(String botName, String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        // геттер имени бота
        return this.botName;
    }

    @Override
    public String getBotToken() {
        // геттер токена бота
        return this.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Проверяем появление нового сообщения в чате, и если это текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();            // Создаем переменную равную тексту присланного сообщения
            String chat_id = update.getMessage().getChatId().toString();    // Создаем переменную равную ид чата присланного сообщения
System.out.println(chat_id);
            for (int i = 0; i < 1000; i++) {
                Test_krisha();

//                try {
//                    getproducts("planshety-noutbuki-kompyutery");
//                    getproducts("telefony-eed");
//                    getproducts("igry-konsoli-i-razvlecheniya");
//                    getproducts("televizory-audio-video");
//                    getproducts("tehnika-dlya-doma");
//                    getproducts("klimat-tehnika");
//                    getproducts("krasota-i-zdorove");
//                    getproducts("kuhonnaya-tehnika");
//                    getproducts("vstraivaemaya-tehnika");
//                    getproducts("posuda-i-aksessuary");
//                    getproducts("foto-videokamery-optika");
//                    getproducts("avtoaksessuary");
//                    getproducts("aktivnyy-otdyh");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//
////                SendMessage message = new SendMessage(); // Создаем обект-сообщение
////                message.setChatId(chat_id);              // Передаем чат ид
////                message.setText(message_text + " " + message.getChatId());           // Передаем эхо сообщение
////
////                try {
////                    execute(message);                   // Отправляем обект-сообщение пользователю
////                } catch (TelegramApiException e) {
////                    e.printStackTrace();
////                }

                pause(60 * 15);
            }

        }
    }


    public void getproducts(String section) throws IOException, SQLException {

        Connection conn = null;
        Statement stmt = null;

        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "18350");
        properties.put("useUnicode", "true");
        properties.put("charSet", "utf8");
        //STEP 2: Register JDBC driver
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            Class.forName('my.sql.Driver');

        //STEP 3: Open a connection
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ali?useSSL=false", properties);

        //STEP 4: Execute a query


        Statement st = conn.createStatement();
        st.execute("set character set utf8");

        boolean last_page = false;
        int page = 1;
        int page_limit = 400;

        while (last_page == false) {
            String urlString = "https://www.mechta.kz/api/v1/catalog?properties=&page=" + page + "&page_limit=" + page_limit + "&section=" + section + "&cache_city=s1";

            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(con.getInputStream());
                JsonNode dataNode = rootNode.get("data");
                JsonNode itemsNode = dataNode.get("items");
                int page_items_count = dataNode.get("page_items_count").asInt();
                System.out.println("page:" + page + " page_items_count: " + page_items_count);

                if (page_items_count < page_limit) {
                    last_page = true; // установка значения last_page в true для выхода из цикла
                }


                for (JsonNode itemNode : itemsNode) {
                    // Доступ к полям элемента массива
                    String id = itemNode.get("id").asText();
                    int price = itemNode.get("price").asInt(); // получаем цену от сервиса
                    int price_from_bd = 0;
                    String name = itemNode.get("name").asText();
                    name = name.replace("'", "");

                    try {
                        ResultSet rs;
                        rs = st.executeQuery("SELECT price " +
                                "FROM prices " +
                                "WHERE product_id=" + id + " ORDER BY created_at DESC LIMIT 1;");
                        rs.next();
                        price_from_bd = Integer.parseInt(rs.getString(1));
                    } catch (SQLException ex) {
//                System.out.println(SQLStr);
//                ex.printStackTrace();Test1234
                    }

//                            parseInt(test_BD.main("SELECT", "SELECT price " +
//                            "FROM prices " +
//                            "WHERE product_id="+id+" ORDER BY created_at DESC LIMIT 1;")); // получаем последнюю цену из БД
                    if (price_from_bd == 0) { // если в бд нет товара, то вносим товар и первую цену

                        String code = itemNode.get("code").asText();
                        int base_price = itemNode.get("prices").get("base_price").asInt();
                        String brand = itemNode.get("metrics").get("brand").asText();
                        brand = brand.replace("'", "");
                        String category = itemNode.get("metrics").get("category").asText();
                        JsonNode photos = itemNode.get("photos");
                        String photo = photos.get(0).asText();


                        System.out.println("id: " + id + ", name: " + name + ", base_price: " + base_price + ", brand: " + brand + ", category: " + category);

//                        SendMessage message = new SendMessage(); // Создаем обект-сообщение
//                        message.setChatId("72313465");              // Передаем чат ид
//                        message.setText(name);           // Передаем эхо сообщение

//                        try {
//                            execute(message);                   // Отправляем обект-сообщение пользователю
//                        } catch (TelegramApiException e) {
//                            e.printStackTrace();
//                        }

                        st.executeUpdate("INSERT INTO ali.products\n" +
                                "(id, name, brand, category, photo, code)\n" +
                                "VALUES(" + id + ", '" + name + "', '" + brand + "', '" + category + "', '" + photo + "', '" + code + "');");

                        st.executeUpdate("INSERT INTO ali.prices" +
                                "(product_id, price)" +
                                "VALUES(" + id + ", " + price + ");");


                    }
                    //ТУТ надо добивить модуль с ценами по скидке

                    //

                    if (price_from_bd > price * 1.2) { // если цена меньше
                        System.out.println("price_from_bd:" + price_from_bd + " price: " + price);
                        String photo_url = "";
                        String product_url = "";
                        try {
                            ResultSet photo_url_bd;
                            ResultSet product_url_bd;
                            photo_url_bd = st.executeQuery("SELECT photo " +
                                    "FROM products " +
                                    "WHERE id=" + id);
                            photo_url_bd.next();
                            photo_url = (photo_url_bd.getString(1));

                            product_url_bd = st.executeQuery("SELECT code " +
                                    "FROM products " +
                                    "WHERE id=" + id);
                            product_url_bd.next();
                            product_url = (product_url_bd.getString(1));

                            System.out.println("photo_url " + photo_url + " product_url: https://www.mechta.kz/product/" + product_url);
                        } catch (SQLException ex) {
//                System.out.println(SQLStr);
//                ex.printStackTrace();
                        }

                        int price_down_potsent =((price_from_bd - price)*100)/price_from_bd;
                        System.out.println("price_down_potsent: " + price_down_potsent);
                        SendPhoto answer = new SendPhoto();
                        answer.setChatId("72313465");
                        answer.setPhoto(new InputFile(photo_url));
                        answer.setCaption("\uD83D\uDC9A: " + name + "\n" +
                                "\n" + "new price: " + price + " (-" + price_down_potsent + "%)" +
                                "\n" + "old price: " + price_from_bd +
                                "\n" + "\n" + "product_url: https://www.mechta.kz/product/" + product_url);


                        try {
                            execute(answer);                   // Отправляем обект-сообщение пользователю
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    if (price_from_bd != price) { // если цена отличается, то вносим новую цену
                        System.out.println("price_from_bd:" + price_from_bd + " price: " + price);
                        st.executeUpdate("INSERT INTO ali.prices" +
                                "(product_id, price)" +
                                "VALUES(" + id + ", " + price + ");");
                    }

                }

                page = page + 1;
            } else {
                System.out.println("Ошибка HTTP-запроса: " + responseCode);
            }


        }

        st.close();
        conn.close();
    }

    public static void pause(long sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void Test_krisha() {

        WebDriverRunner.setWebDriver(getChrome2());
        String ingigo = "";




        for (int s = 0; s < 1000; s++) {

            ingigo = test_indigo.place("10081");
            if (ingigo.length() > 1) {
                sendMsg2(ingigo, "72313465");
                sendMsg2(ingigo, "190692261");
            }
            System.out.println("*");
            ingigo = test_indigo.place("10082");
            if (ingigo.length() > 1) {
                sendMsg2(ingigo, "72313465");
                sendMsg2(ingigo, "190692261");
            }
            System.out.println("*");
            ingigo = test_indigo.place("30217");
            if (ingigo.length() > 1) {
                sendMsg2(ingigo, "72313465");
                sendMsg2(ingigo, "190692261");
            }
            System.out.println("*");

            System.out.println("*"); }



        WebDriverRunner.closeWebDriver();
    }
    public Integer sendMsg2(String text, String ChatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setText(text);
        sendMessage
                .setChatId(ChatId);
        Message Mes_id = null;
        try {
            execute(sendMessage);                   // Отправляем обект-сообщение пользователю
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return Mes_id.getMessageId();




    }

    public static WebDriver getChrome2() {
        final ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("chromedriver.exe"))
//                .usingDriverExecutable(new File("operadriver.exe"))
                .usingAnyFreePort()
                .build();
        final ChromeOptions option = new ChromeOptions();
        option.addArguments("--always-authorize-plugins", "--start-maximized", "--scroll-end-effect=1")
                .setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe")
       ;
        EventFiringWebDriver newDriver = new EventFiringWebDriver(new ChromeDriver(service, option));
//        newDriver.register(new ListenerThatHiglilightsElements("#FFFF00", 3, 30, TimeUnit.MILLISECONDS));
        return newDriver;


    }
}
