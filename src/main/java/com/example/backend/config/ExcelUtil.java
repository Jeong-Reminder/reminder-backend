package com.example.backend.config;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    public static List<Member> readMembersFromExcel(InputStream is) throws IOException {
        List<Member> members = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // header row

            Member member = new Member();
            member.setStudentId(row.getCell(0).getStringCellValue());
            member.setName(row.getCell(1).getStringCellValue());
            member.setLevel((int) row.getCell(2).getNumericCellValue());
            member.setStatus(row.getCell(3).getStringCellValue());
            member.setUserRole(UserRole.ROLE_USER);

            members.add(member);
        }

        workbook.close();
        return members;
    }
}
