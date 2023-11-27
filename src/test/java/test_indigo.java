import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.*;


public class test_indigo {

    public static String place(String sad) {

        String info = "", age;
//        WebDriverRunner.setWebDriver(getChrome2());

        open("https://indigo-nursultan.e-orda.kz/ru/view/"+sad);
        pause(20000);

        for (int i = 0; i < $$(byClassName("gn-inner")).size(); i++) {
            System.out.println(i);
            age=$$(byClassName("gn-inner")).get(i).$(byClassName("gn-d-name")).getText();
            System.out.println(age);
            if (info=="" & (age.contains("от 3 до 4 лет") || age.contains("от 3 до 5 лет"))) {
                info = $$(byClassName("gn-inner")).get(i).$(byClassName("gn-d-info")).getText();
               System.out.println(age);

                if (!info.contains("Общего доступа: 0")) {
                    info=$(byClassName("body-title")).getText()+"\n"+age+"\n"+info+"\nhttps://indigo-nursultan.e-orda.kz/ru/view/"+sad;
                    info="\uD83C\uDF53\uD83C\uDF51\uD83C\uDF52\uD83C\uDF4E\uD83C\uDF4A\uD83C\uDF4D\uD83E\uDD5D\uD83E\uDD65\uD83C\uDF3D\uD83E\uDD51\uD83C\uDF55\uD83C\uDF54 \n"+info;
                    System.out.println(info);
                } else {info="";}
            }

        }

        pause(2000);
//        WebDriverRunner.closeWebDriver();

return info;
    }

    public static void main(String[] args) {

    }

    public static void pause(long sec) {
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
