import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

import static java.lang.Integer.parseInt;

public class GetRequestExample {


    public static void main(String[] args) throws IOException, SQLException {
        getproducts("planshety-noutbuki-kompyutery");
        getproducts("telefony-eed");
        getproducts("igry-konsoli-i-razvlecheniya");
        getproducts("televizory-audio-video");
        getproducts("tehnika-dlya-doma");
        getproducts("klimat-tehnika");
        getproducts("krasota-i-zdorove");
        getproducts("kuhonnaya-tehnika");
        getproducts("vstraivaemaya-tehnika");
        getproducts("posuda-i-aksessuary");
        getproducts("foto-videokamery-optika");
        getproducts("avtoaksessuary");
        getproducts("aktivnyy-otdyh");
//        getproducts("salfetki-zkp");
    }

    public static void getproducts(String section) throws IOException, SQLException {




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

        boolean last_page=false;
        int page=1;
        int page_limit=400;

        while (last_page==false) {
            String urlString = "https://www.mechta.kz/api/v1/catalog?properties=&page="+page+"&page_limit="+page_limit+"&section="+section+"&cache_city=s1";

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
                System.out.println("page:"+page+" page_items_count: " + page_items_count);

                if (page_items_count<page_limit) {
                    last_page = true; // установка значения last_page в true для выхода из цикла
                }


                for (JsonNode itemNode : itemsNode) {
                    // Доступ к полям элемента массива
                    String id = itemNode.get("id").asText();
                    int price = itemNode.get("price").asInt(); // получаем цену от сервиса
                    int price_from_bd=0;


                    try {
                    ResultSet rs;
                    rs=st.executeQuery("SELECT price " +
                            "FROM prices " +
                            "WHERE product_id="+id+" ORDER BY created_at DESC LIMIT 1;");
                    rs.next();
                    price_from_bd= Integer.parseInt(rs.getString(1));
                    } catch (SQLException ex) {
//                System.out.println(SQLStr);
//                ex.printStackTrace();
                    }

//                            parseInt(test_BD.main("SELECT", "SELECT price " +
//                            "FROM prices " +
//                            "WHERE product_id="+id+" ORDER BY created_at DESC LIMIT 1;")); // получаем последнюю цену из БД
                    if (price_from_bd==0) {

                        String name = itemNode.get("name").asText();
                        name=name.replace("'", "");
                        String code = itemNode.get("code").asText();
                        int base_price = itemNode.get("prices").get("base_price").asInt();
                        String brand = itemNode.get("metrics").get("brand").asText();
                        brand=brand.replace("'", "");
                        String category = itemNode.get("metrics").get("category").asText();
                        JsonNode photos = itemNode.get("photos");
                        String photo = photos.get(0).asText();


                        System.out.println("id: " + id + ", name: " + name + ", base_price: " + base_price+ ", brand: " + brand+ ", category: " + category);


                        st.executeUpdate("INSERT INTO ali.products\n" +
                                "(id, name, brand, category, photo, code)\n" +
                                "VALUES("+ id +", '"+ name +"', '"+ brand+"', '"+ category +"', '"+ photo +"', '"+ code +"');");

                        st.executeUpdate("INSERT INTO ali.prices" +
                                "(product_id, price)" +
                                "VALUES("+ id +", "+price+");");

                    }




                }

                page=page+1;
            } else {
                System.out.println("Ошибка HTTP-запроса: " + responseCode);
            }


        }

        st.close();
        conn.close();
    }

}