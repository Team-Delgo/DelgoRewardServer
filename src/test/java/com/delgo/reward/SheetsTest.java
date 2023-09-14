package com.delgo.reward;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.sheets.SheetsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SheetsTest {

    @Autowired
    private SheetsService sheetsService;

    @Test
    public void getSheetsTEST() throws GeneralSecurityException, IOException {
        sheetsService.getSheetsData(CategoryCode.CA0002);
    }
}