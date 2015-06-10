package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {

    //Нужно для поиска среди пользователей
    public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);

    @Id
    @Column(unique = true) //Надо, чтобы поле в базе данных было уникальным
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="user_seq") //Надо, чтобы айдишники пользователя шли в нормальном порядке
    private Integer userId; //Айди пользователя

    @Constraints.Required
    @Column(unique=true)
    private String userLogin; //Логин пользователя

    private String userFirstName; //Имя пользователя

    private String userLastName; //Фамилия пользователя

    private Date regDate; //Дата регистрации пользователя

    @Constraints.Required
    private String userPass; //Пароль пользователя

    @OneToMany(cascade=CascadeType.ALL, mappedBy="interviewOwner")
    private List<Interview> interviews; //Список опросов пользователя

    //Пустой конструктор пользователя
    public User() {
    }

    //Конструктор пользователя
    public User(String userLogin, String userFirstName, String userLastName, String userPass) {
        this.userLogin = userLogin;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userPass = userPass;
        this.regDate = new java.util.Date(); //Дата регистрации ставится автоматически при создании пользователя
    }

    // Геттеры и сеттеры экземпляра класса, требуются для того
    // чтобы, можно было получать или изменять поля экземпляра класса

    public Integer getUserId() {
        return userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public Date getRegDate() {
        return regDate;
    }

    public List<Interview> getInterviews() {
        return interviews;
    }

    //Метод, позволяющий получать данные пользователя сразу в формате JSON, приемлимым для отправки клиенту в браузер
    public ObjectNode getUserInfoJSON() {
        ObjectNode getUserInfoJSON = Json.newObject();
        DateFormat date = new SimpleDateFormat("dd.MM.yy HH:mm"); //задаем вид даты и времени

        getUserInfoJSON.put("userId", this.userId); //Кладем каждое поле в JSON объект
        getUserInfoJSON.put("userLogin", this.userLogin);
        getUserInfoJSON.put("userFirstName", this.userFirstName);
        getUserInfoJSON.put("userLastName", this.userLastName);
        getUserInfoJSON.put("regDate", date.format(this.regDate)); //тут еще мы преобразовываем время в пользовательский вид

        return getUserInfoJSON;
    }
}
