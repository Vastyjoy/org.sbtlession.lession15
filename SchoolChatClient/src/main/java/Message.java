import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String userName;
    private LocalDateTime date;
    private String message;

    public Message(String userName, String message) {
        this.userName = userName;
        this.date = LocalDateTime.now();
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getSerName() {
        String dateStr = date.toString();
        dateStr = dateStr.replaceAll(":", "-");
        return userName + '-' + dateStr + '-' + message;
    }

    @Override
    public String toString() {
        return "Message:" +
                " " + date +
                " " + userName +
                " : " + message + '\n';
    }

}
