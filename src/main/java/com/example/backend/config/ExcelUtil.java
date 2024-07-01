package com.example.backend.config;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
            member.setStudentId(getCellValueAsString(row.getCell(0)));
            member.setName(getCellValueAsString(row.getCell(1)));
            member.setLevel((int) getCellValueAsNumeric(row.getCell(2)));
            member.setStatus(getCellValueAsString(row.getCell(3)));
            member.setUserRole(UserRole.ROLE_USER);

            members.add(member);
        }

        workbook.close();
        return members;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        return null;
    }

    private static double getCellValueAsNumeric(Cell cell) {
        if (cell == null) {
            return 0;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}