package Core;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    static final long SerialVersionUID= 123456;
    public enum messageType{
            SuccessRequest, UnSuccessRequest, Authorization,GetMessage,SendMessage;
          }
    private String sourceName;
    private String destinationName;
    private LocalDateTime date;
    private String message;
    private messageType messageType;

    public Message(String sourceName, String destinationName, String message, Message.messageType messageType) {
        this.sourceName=sourceName;
        this.destinationName=destinationName;
        this.messageType = messageType;
        this.date = LocalDateTime.now();
        this.message = message;
    }

    public Message.messageType getMessageType() {
        return messageType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return "Core.Message:" +
                " От:" + sourceName +
                " Кому:" + destinationName +
                " -> " + message + '\n';
    }

}
