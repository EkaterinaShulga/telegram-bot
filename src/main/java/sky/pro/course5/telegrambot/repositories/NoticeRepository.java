package sky.pro.course5.telegrambot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.pro.course5.telegrambot.model.Notice;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    default List<Notice> findNoticeByDateAndTime(List<Notice> notices) {
        List<Notice> allSuitableNotice = new ArrayList<>();
        LocalDateTime timeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        for (Notice notice : notices) {
            if (notice.getDateAndTimeSendNotice().equals(timeNow)) {
                allSuitableNotice.add(notice);

            }
        }
        return allSuitableNotice;

    }
}










