package edu.illinois.dscs.mypocket.model;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Formula;
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

    public File saveAsXLS() throws WriteException, IOException {
        WriteExcel myPocketWriter = new WriteExcel();
        myPocketWriter.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/MyPocket.xls");
        myPocketWriter.write();
        return myPocketWriter.getFile();
    }

    public static class WriteExcel {

        private WritableCellFormat timesBoldUnderline;
        private WritableCellFormat times;
        private File file;

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
            createLabel(excelSheet);
            addTransactionLabels(excelSheet);
            createContent(excelSheet);
            resizeColumns(excelSheet, 5);
        }

        private void writeAccountContentOn(WritableSheet excelSheet) throws WriteException {
            createLabel(excelSheet);
            addAccountLabels(excelSheet);
            createContent(excelSheet);
            resizeColumns(excelSheet, 4);
        }

        private void writeCategoryContentOn(WritableSheet excelSheet) throws WriteException {
            createLabel(excelSheet);
            addCategoryLabels(excelSheet);
            createContent(excelSheet);
            resizeColumns(excelSheet, 1);
        }

        private void addTransactionLabels(WritableSheet sheet) throws WriteException {
            addCaption(sheet, 0, 0, "Description");
            addCaption(sheet, 1, 0, "Value");
            addCaption(sheet, 2, 0, "Date");
            addCaption(sheet, 3, 0, "Account Name");
            addCaption(sheet, 4, 0, "Category");
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

        private void createLabel(WritableSheet sheet) throws WriteException {

            // Times font for labels

            WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
            times = new WritableCellFormat(times10pt);
            times.setWrap(true);

            // Bold Times font for captions

            WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD);
            timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
            timesBoldUnderline.setWrap(true);
        }

        private void createContent(WritableSheet sheet) throws WriteException {
            // Write a few number
            for (int i = 1; i < 10; i++) {
                // First column
                addNumber(sheet, 0, i, i + 10);
                // Second column
                addNumber(sheet, 1, i, i * i);
            }
            // Lets calculate the sum of it
            StringBuffer buf = new StringBuffer();
            buf.append("SUM(A2:A10)");
            Formula f = new Formula(0, 10, buf.toString());
            sheet.addCell(f);
            buf = new StringBuffer();
            buf.append("SUM(B2:B10)");
            f = new Formula(1, 10, buf.toString());
            sheet.addCell(f);

            // now a bit of text
            for (int i = 12; i < 20; i++) {
                // First column
                addLabel(sheet, 0, i, "Boring text " + i);
                // Second column
                addLabel(sheet, 1, i, "Another text");
            }
        }

        private void addCaption(WritableSheet sheet, int column, int row, String s)
                throws WriteException {
            Label label = new Label(column, row, s, timesBoldUnderline);
            sheet.addCell(label);
        }

        private void addNumber(WritableSheet sheet, int column, int row, Integer integer)
                throws WriteException {
            Number number = new Number(column, row, integer, times);
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
}
