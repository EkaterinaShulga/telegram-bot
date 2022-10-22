package sky.pro.course5.telegrambot;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.course5.telegrambot.model.Notice;
import sky.pro.course5.telegrambot.repositories.NoticeRepository;
import sky.pro.course5.telegrambot.service.TelegramBotUpdatesListener;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    public TelegramBotUpdatesListenerTest() {
    }

    private Notice one;
    private Notice two;
    private List<Notice> notices;
    private List<Notice> greetingNotice;

    @BeforeEach
    public void setUp() {
        notices = new ArrayList<>();
        greetingNotice = new ArrayList<>();
        one = new Notice("Сдать курсовую", 8437932L,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 16, 30, 00));
        two = new Notice("Подготовиться к собеседованию", 8437932L,
                LocalDateTime.of(2022, Month.DECEMBER, 21, 10, 15, 00));
        notices.add(one);
        notices.add(two);
        greetingNotice.add(one);
    }


    @Test
    public void getGreetingNotice() {
        Mockito.when(this.noticeRepository.findNoticeByDateAndTime(notices)).thenReturn(greetingNotice);
        Assertions.assertEquals(greetingNotice, this.noticeRepository.findNoticeByDateAndTime(notices));
    }

    @Test
    public void getAllNotices() {
        Mockito.when(this.noticeRepository.findAll()).thenReturn(notices);
        Assertions.assertEquals(notices, this.noticeRepository.findAll());
    }


}