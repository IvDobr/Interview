import models.User;
import play.Application;
import play.GlobalSettings;

//Данный класс выполняется кадый раз при запуске приложения
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {

        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8")); //Задаем иркутский часовой пояс

        if (User.find.all().isEmpty()) { //проверяем, пустой ли список пользователей
            User user = new User("user", "Тестовый", "Пользователь", "123"); //создаем экземпляр нового пользователя
            try {
                user.save(); //сохраняем его
                System.out.println("DONE: Аккаунт пользователя создан!");
            } catch (Exception e) {
                System.out.println("ERROR: Невозможно создать аккаунт пользователя!");
            }
        } else {System.out.println("MSG: Один пользователь уже есть, первичное заполнение исполнено");}
    }
}