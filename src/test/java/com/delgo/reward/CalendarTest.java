package com.delgo.reward;

import com.delgo.reward.dto.CalendarDTO;
import com.delgo.reward.service.CalendarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalendarTest {

    @Autowired
    private CalendarService calendarService;

    @Test
    public void makeCalendarDataTest() {
        //given
        int userId = 0;

        //when
        List<CalendarDTO> calendarList = calendarService.getCalendar(userId);
        for (CalendarDTO calendar : calendarList) {
            System.out.println("calendar : " + calendar);
        }

        //then
        assertTrue(calendarList.size() > 0);
    }
}