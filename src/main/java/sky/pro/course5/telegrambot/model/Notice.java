package sky.pro.course5.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String textNotice;
    private long chatId;
    private LocalDateTime dateAndTimeSendNotice;

    public Notice() {
    }

    public Notice(String textNotice, long chatId, LocalDateTime dateAndTimeSendNotice) {
        this.textNotice = textNotice;
        this.chatId = chatId;
        this.dateAndTimeSendNotice = dateAndTimeSendNotice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDateAndTimeSendNotice() {
        return dateAndTimeSendNotice;
    }

    public void setDateAndTimeSendNotice(LocalDateTime dateAndTimeSendNotice) {
        this.dateAndTimeSendNotice = dateAndTimeSendNotice;
    }

    public String getTextNotice() {
        return textNotice;
    }

    public void setTextNotice(String textNotice) {
        this.textNotice = textNotice;
    }

}

