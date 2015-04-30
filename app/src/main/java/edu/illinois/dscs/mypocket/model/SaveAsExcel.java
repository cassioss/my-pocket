package edu.illinois.dscs.mypocket.model;

import android.database.Cursor;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import edu.illinois.dscs.mypocket.dao.DBHelper;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * @author Cassio
 * @version 1.0
 */
public class SaveAsExcel {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private File file;
    Cursor transactionCursor, accountCursor, categoryCursor;

    public void setCursors(Cursor transactionCursor, Cursor accountCursor, Cursor categoryCursor) {
        this.transactionCursor = transactionCursor;
        this.accountCursor = accountCursor;
        this.categoryCursor = categoryCursor;
    }

    public void setOutputFile(String inputFile) {
        this.file = new File(inputFile);
    }

    public File getFile() {
        return this.file;
    }

    public void write() throws IOException, WriteException {

        // Creates a new workbook (a.k.a. initializes the Excel file)

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

        // Creates a spreadsheet for each table

        workbook.createSheet("Transactions", 0);
        workbook.createSheet("Accounts", 1);
        workbook.createSheet("Categories", 2);

        // Creates proper formatting

        formatLabels();

        // Writes content on each table

        WritableSheet transactionSheet = workbook.getSheet(0);
        WritableSheet accountSheet = workbook.getSheet(1);
        WritableSheet categorySheet = workbook.getSheet(2);

        writeTransactionContentOn(transactionSheet);
        writeAccountContentOn(accountSheet);
        writeCategoryContentOn(categorySheet);

        // Writes the data on the workbook and closes the file

        workbook.write();
        workbook.close();
    }

    private void writeTransactionContentOn(WritableSheet excelSheet) throws WriteException {
        addTransactionLabels(excelSheet);
        createTransactionContent(excelSheet);
        resizeColumns(excelSheet, 6);
    }

    private void writeAccountContentOn(WritableSheet excelSheet) throws WriteException {
        addAccountLabels(excelSheet);
        createAccountContent(excelSheet);
        resizeColumns(excelSheet, 4);
    }

    private void writeCategoryContentOn(WritableSheet excelSheet) throws WriteException {
        addCategoryLabels(excelSheet);
        createCategoryContent(excelSheet);
        resizeColumns(excelSheet, 1);
    }

    private void addTransactionLabels(WritableSheet sheet) throws WriteException {
        addCaption(sheet, 0, 0, "Type");
        addCaption(sheet, 1, 0, "Description");
        addCaption(sheet, 2, 0, "Value");
        addCaption(sheet, 3, 0, "Date");
        addCaption(sheet, 4, 0, "Account Name");
        addCaption(sheet, 5, 0, "Category");
    }

    private void addAccountLabels(WritableSheet sheet) throws WriteException {
        addCaption(sheet, 0, 0, "Name");
        addCaption(sheet, 1, 0, "Initial Value");
        addCaption(sheet, 2, 0, "Current Balance");
        addCaption(sheet, 3, 0, "Active");
    }

    private void addCategoryLabels(WritableSheet sheet) throws WriteException {
        addCaption(sheet, 0, 0, "Name");
    }

    private void formatLabels() throws WriteException {
        // Times font for labels
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        times = new WritableCellFormat(times10pt);
        times.setWrap(true);

        // Bold Times font for captions
        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        timesBoldUnderline.setWrap(true);
    }

    private void createTransactionContent(WritableSheet sheet) throws WriteException {
        int row = 1;
        while (!transactionCursor.isAfterLast()) {
            Integer typeValue = Integer.valueOf(transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_TRANS_TYPE)));
            String type = typeValue > 0 ? "Income" : "Expense";

            addLabel(sheet, 0, row, type);
            addLabel(sheet, 1, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_TRANS_DESCRIPTION)));
            addNumber(sheet, 2, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_TRANS_VALUE)));
            addLabel(sheet, 3, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_TRANS_CREATION_DATE)));
            addLabel(sheet, 4, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME)));
            addLabel(sheet, 5, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_CATEGORY_NAME)));

            row++;
            transactionCursor.moveToNext();
        }
    }

    private void createAccountContent(WritableSheet sheet) throws WriteException {
        int row = 1;
        while (!accountCursor.isAfterLast()) {
            Integer isActive = Integer.valueOf(accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_ACTIVE)));
            String yesNo = isActive > 0 ? "Yes" : "No";

            addLabel(sheet, 0, row, accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME)));
            addNumber(sheet, 1, row, accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_INITIAL_VALUE)));
            addNumber(sheet, 2, row, accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE)));
            addLabel(sheet, 3, row, yesNo);

            row++;
            accountCursor.moveToNext();
        }
    }

    private void createCategoryContent(WritableSheet sheet) throws WriteException {
        int row = 1;
        while (!categoryCursor.isAfterLast()) {
            addLabel(sheet, 0, row, categoryCursor.getString(categoryCursor.getColumnIndex(DBHelper.KEY_CATEGORY_NAME)));
            row++;
            categoryCursor.moveToNext();
        }
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws WriteException {
        Label label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row, String s)
            throws WriteException {
        Number number = new Number(column, row, Double.valueOf(s), times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException {
        Label label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    private void resizeColumns(WritableSheet sheet, int columnNumber) throws WriteException {
        for (int column = 0; column < columnNumber; column++) {
            CellView cell = sheet.getColumnView(column);
            cell.setAutosize(true);
            sheet.setColumnView(column, cell);
        }
    }
}