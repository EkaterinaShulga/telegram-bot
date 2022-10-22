package sky.pro.course5.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sky.pro.course5.telegrambot.model.Notice;
import sky.pro.course5.telegrambot.repositories.NoticeRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final String NOTICE_TEXT_PATTERN = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";
    private final Pattern pattern = Pattern.compile(NOTICE_TEXT_PATTERN);

    private final NoticeRepository noticeRepository;

    public TelegramBotUpdatesListener(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            TelegramBot bot = new TelegramBot(telegramBot.getToken());
            if (update.message() != null) {
                long chatId = update.message().chat().id();
                String messageText = "Привет, я бот.";
                SendMessage message = new SendMessage(chatId, messageText);
                String st = "/start";
                if (update.message().text().equals(st)) {
                    SendResponse response = bot.execute(message);
                    if (response.isOk()) {
                        System.out.println("Cообщение отправлено");
                    } else {
                        System.out.println("Сообщение не было отправлено, код ошибки " + response.errorCode());
                    }
                } else if (!update.message().text().equals(st) && update.message() != null) {
                    createNotice(update);
                }
            } else {
                logger.warn("Что-то пошло не так");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void createNotice(Update update) {
        TelegramBot bot = new TelegramBot(telegramBot.getToken());
        String text = update.message().text();
        long chatId = update.message().chat().id();
        String model = "01.01.2022 20:00 Сделать домашнюю работу";
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String dateAndTimeString = matcher.group(1);
            String item = matcher.group(3);
            LocalDateTime dateAndTime = LocalDateTime.parse(dateAndTimeString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            Notice notice = new Notice();
            notice.setTextNotice(item);
            notice.setChatId(chatId);
            notice.setDateAndTimeSendNotice(dateAndTime);
            noticeRepository.save(notice);
            String messageForBase = "Я сохранил вашу задачу в базе данных.";
            SendMessage messageInfo = new SendMessage(chatId, messageForBase);
            bot.execute(messageInfo);
            logger.info("Данные сохранены в базе данных");
        } else {
            String warningMessage = "Не смогу сохранить запись. Введенное сообщение не соотствует шаблону: "
                    + model + ". Попробуйте еще раз.";
            SendMessage messageForAnswer = new SendMessage(chatId, warningMessage);
            bot.execute(messageForAnswer);
            logger.warn("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону");
        }
    }


    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotice() {
        TelegramBot bot = new TelegramBot(telegramBot.getToken());
        List<Notice> allNotice = noticeRepository.findAll();
        List<Notice> allSuitableNotice = noticeRepository.findNoticeByDateAndTime(allNotice);
        for (Notice notice : allSuitableNotice) {
            SendMessage message = new SendMessage(notice.getChatId(), notice.getTextNotice());
            bot.execute(message);
        }
    }


}