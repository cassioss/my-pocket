package edu.illinois.dscs.mypocket.model;

import android.database.Cursor;
import android.os.Environment;

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

    /**
     * Sets all the required query cursors.
     *
     * @param transactionCursor the cursor resulting from querying the Transactions table.
     * @param accountCursor     the cursor resulting from querying the Account table.
     * @param categoryCursor    the cursor resulting from querying the Category table.
     */
    public void setCursors(Cursor transactionCursor, Cursor accountCursor, Cursor categoryCursor) {
        this.transactionCursor = transactionCursor;
        this.accountCursor = accountCursor;
        this.categoryCursor = categoryCursor;
    }

    /**
     * Sets an output file on the Download (internal) folder, already set as XLS.
     *
     * @param fileName the file name.
     */
    public void setOutputFile(String fileName) {
        this.file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName + ".xls");
    }

    /**
     * Gets the file that's being accessed to save the data.
     *
     * @return a File object that references the XLS files.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Writes all content present on the database.
     *
     * @throws IOException    if accessing the XLS file is impossible.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
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

    /**
     * Writes the content of the Transactions table in a specific spreadsheet.
     *
     * @param excelSheet the specified spreadsheet inside the XLS file.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void writeTransactionContentOn(WritableSheet excelSheet) throws WriteException {
        addTransactionCaptions(excelSheet);
        createTransactionContent(excelSheet);
        resizeColumns(excelSheet, 6);
    }

    /**
     * Writes the content of the Account table in a specific spreadsheet.
     *
     * @param excelSheet the specified spreadsheet inside the XLS folder.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void writeAccountContentOn(WritableSheet excelSheet) throws WriteException {
        addAccountCaptions(excelSheet);
        createAccountContent(excelSheet);
        resizeColumns(excelSheet, 4);
    }

    /**
     * Writes the content of the Category table in a specific spreadsheet.
     *
     * @param excelSheet the specified spreadsheet inside the XLS folder.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void writeCategoryContentOn(WritableSheet excelSheet) throws WriteException {
        addCategoryCaptions(excelSheet);
        createCategoryContent(excelSheet);
        resizeColumns(excelSheet, 1);
    }

    /**
     * Adds the correct captions to the Transactions spreadsheet.
     *
     * @param sheet a given spreadsheet inside the XLS folder.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void addTransactionCaptions(WritableSheet sheet) throws WriteException {
        addCaption(sheet, 0, 0, "Type");
        addCaption(sheet, 1, 0, "Description");
        addCaption(sheet, 2, 0, "Value");
        addCaption(sheet, 3, 0, "Date");
        addCaption(sheet, 4, 0, "Account Name");
        addCaption(sheet, 5, 0, "Category");
    }

    /**
     * Adds the correct captions to the Account spreadsheet.
     *
     * @param sheet a given spreadsheet inside the XLS folder.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void addAccountCaptions(WritableSheet sheet) throws WriteException {
        addCaption(sheet, 0, 0, "Name");
        addCaption(sheet, 1, 0, "Initial Value");
        addCaption(sheet, 2, 0, "Current Balance");
        addCaption(sheet, 3, 0, "Active");
    }

    /**
     * Adds the correct captions to the Category spreadsheet.
     *
     * @param sheet a given spreadsheet inside the XLS folder.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void addCategoryCaptions(WritableSheet sheet) throws WriteException {
        addCaption(sheet, 0, 0, "Name");
    }

    /**
     * Creates the correct format for the labels, in terms of font type, font size, and so on.
     *
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
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

    /**
     * Turns the content inside the Transactions cursor into a complete table inside the Transactions spreadsheet.
     *
     * @param sheet a given spreadsheet inside the XLS folder.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void createTransactionContent(WritableSheet sheet) throws WriteException {
        int row = 1;
        while (!transactionCursor.isAfterLast()) {
            Integer typeValue = Integer.valueOf(transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_TRANS_TYPE)));
            String type = typeValue > 0 ? "Income" : "Expense";

            addLabel(sheet, 0, row, type);
            addLabel(sheet, 1, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_TRANS_DESCRIPTION)));
            addDouble(sheet, 2, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_TRANS_VALUE)));
            addLabel(sheet, 3, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_TRANS_CREATION_DATE)));
            addLabel(sheet, 4, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME)));
            addLabel(sheet, 5, row, transactionCursor.getString(transactionCursor.getColumnIndex(DBHelper.KEY_CATEGORY_NAME)));

            row++;
            transactionCursor.moveToNext();
        }
    }

    /**
     * Turns the content inside the Account cursor into a complete table inside the Account spreadsheet.
     *
     * @param sheet a given spreadsheet inside the XLS folder.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void createAccountContent(WritableSheet sheet) throws WriteException {
        int row = 1;
        while (!accountCursor.isAfterLast()) {
            Integer isActive = Integer.valueOf(accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_ACTIVE)));
            String yesNo = isActive > 0 ? "Yes" : "No";

            addLabel(sheet, 0, row, accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME)));
            addDouble(sheet, 1, row, accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_INITIAL_VALUE)));
            addDouble(sheet, 2, row, accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE)));
            addLabel(sheet, 3, row, yesNo);

            row++;
            accountCursor.moveToNext();
        }
    }

    /**
     * Turns the content inside the Category cursor into a complete table inside the Category spreadsheet.
     *
     * @param sheet a given spreadsheet inside the XLS folder.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void createCategoryContent(WritableSheet sheet) throws WriteException {
        int row = 1;
        while (!categoryCursor.isAfterLast()) {
            addLabel(sheet, 0, row, categoryCursor.getString(categoryCursor.getColumnIndex(DBHelper.KEY_CATEGORY_NAME)));
            row++;
            categoryCursor.moveToNext();
        }
    }

    /**
     * Adds a caption to a given cell inside a spreadsheet.
     *
     * @param sheet   a given spreadsheet inside the XLS folder.
     * @param column  a specific column number, starting from 0.
     * @param row     a specific row number, starting from 0.
     * @param caption a given string to be set as caption.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void addCaption(WritableSheet sheet, int column, int row, String caption)
            throws WriteException {
        Label newCaption = new Label(column, row, caption, timesBoldUnderline);
        sheet.addCell(newCaption);
    }

    /**
     * Adds a double number to a given cell inside a spreadsheet.
     *
     * @param sheet        a given spreadsheet inside the XLS folder.
     * @param column       a specific column number, starting from 0.
     * @param row          a specific row number, starting from 0.
     * @param doubleString a given string that can be parsed as a Double number.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void addDouble(WritableSheet sheet, int column, int row, String doubleString)
            throws WriteException {
        Number number = new Number(column, row, Double.valueOf(doubleString), times);
        sheet.addCell(number);
    }

    /**
     * Adds a label to a given cell inside a spreadsheet.
     *
     * @param sheet  a given spreadsheet inside the XLS folder.
     * @param column a specific column number, starting from 0.
     * @param row    a specific row number, starting from 0.
     * @param label  a given string to be set as label.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void addLabel(WritableSheet sheet, int column, int row, String label)
            throws WriteException {
        Label newLabel = new Label(column, row, label, times);
        sheet.addCell(newLabel);
    }

    /**
     * Resizes a given number of columns inside a spreadsheet, starting from the leftmost. Specifying the number of columns controls the amount of resources needed for this operation.
     *
     * @param sheet        a given spreadsheet inside the XLS folder.
     * @param numOfColumns a specific number of columns (starting from 0) that needs to be reformatted.
     * @throws WriteException if it is not possible to write some content on the XLS file.
     */
    private void resizeColumns(WritableSheet sheet, int numOfColumns) throws WriteException {
        for (int column = 0; column < numOfColumns; column++) {
            CellView cell = sheet.getColumnView(column);
            cell.setAutosize(true);
            sheet.setColumnView(column, cell);
        }
    }
}