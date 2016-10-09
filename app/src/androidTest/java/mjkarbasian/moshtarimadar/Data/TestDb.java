package mjkarbasian.moshtarimadar.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;
import java.util.HashSet;

import mjkarbasian.moshtarimadar.Tool.TestUtilities;

/**
 * Created by Unique on 10/3/2016.
 */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(KasebDbHelper.DATABASE_NAME);
    }

    /* This function gets called before each test is executed to delete the database.
       This makes sure that we always have a clean test.*/

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        // region have we created the tables we want?
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        // 1
        tableNameHashSet.add(KasebContract.State.TABLE_NAME);
        // 2
        tableNameHashSet.add(KasebContract.Sales.TABLE_NAME);
        // 3
        tableNameHashSet.add(KasebContract.Products.TABLE_NAME);
        // 4
        tableNameHashSet.add(KasebContract.Costs.TABLE_NAME);
        // 5
        tableNameHashSet.add(KasebContract.PaymentMethods.TABLE_NAME);
        // 6
        tableNameHashSet.add(KasebContract.TaxTypes.TABLE_NAME);
        // 7
        tableNameHashSet.add(KasebContract.ProductHistory.TABLE_NAME);
        // 8
        tableNameHashSet.add(KasebContract.DetailSaleTaxes.TABLE_NAME);
        // 9
        tableNameHashSet.add(KasebContract.DetailSaleProducts.TABLE_NAME);
        // 10
        tableNameHashSet.add(KasebContract.CostTypes.TABLE_NAME);
        // 11
        tableNameHashSet.add(KasebContract.DetailSale.TABLE_NAME);
        // 12
        tableNameHashSet.add(KasebContract.DetailSalePayments.TABLE_NAME);
        // 13
        tableNameHashSet.add(KasebContract.Customers.TABLE_NAME);

        deleteTheDatabase();
        SQLiteDatabase db = new KasebDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());
        //endregion

        // region check to create tables correct
        assertTrue("Error: Your database was created but not all tables",
                tableNameHashSet.isEmpty());
        //endregion

        //  region 1 -State table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.State.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -State table- information.",
                c.moveToFirst());

        final HashSet<String> StateColumnHashSet = new HashSet<String>();
        StateColumnHashSet.add(KasebContract.State.COLUMN_STATE_POINTER);

        int StatecolumnNameIndex = c.getColumnIndex("name");
        do {

            String StatecolumnName = c.getString(StatecolumnNameIndex);
            Log.d(LOG_TAG, StatecolumnName);
            StateColumnHashSet.remove(StatecolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -State table- columns",
                StateColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 2 -Sales table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.Sales.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -Sales table- information.",
                c.moveToFirst());

        final HashSet<String> SalesColumnHashSet = new HashSet<String>();
        SalesColumnHashSet.add(KasebContract.Sales.COLUMN_CUSTOMER_ID);
        SalesColumnHashSet.add(KasebContract.Sales.COLUMN_SALE_CODE);
        SalesColumnHashSet.add(KasebContract.Sales.COLUMN_IS_DELETED);

        int SalescolumnNameIndex = c.getColumnIndex("name");
        do {

            String SalescolumnName = c.getString(SalescolumnNameIndex);
            Log.d(LOG_TAG, SalescolumnName);
            SalesColumnHashSet.remove(SalescolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -Sales table- columns",
                SalesColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 3 -Products table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.Products.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -Products table- information.",
                c.moveToFirst());

        final HashSet<String> ProductsColumnHashSet = new HashSet<String>();
        ProductsColumnHashSet.add(KasebContract.Products.COLUMN_PRODUCT_NAME);
        ProductsColumnHashSet.add(KasebContract.Products.COLUMN_PRODUCT_CODE);
        ProductsColumnHashSet.add(KasebContract.Products.COLUMN_UNIT);
        ProductsColumnHashSet.add(KasebContract.Products.COLUMN_DESCRIPTION);

        int ProductscolumnNameIndex = c.getColumnIndex("name");
        do {

            String ProductscolumnName = c.getString(ProductscolumnNameIndex);
            Log.d(LOG_TAG, ProductscolumnName);
            ProductsColumnHashSet.remove(ProductscolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -Products table- columns",
                ProductsColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 4 -Costs table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.Costs.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -Costs table- information.",
                c.moveToFirst());

        final HashSet<String> CostsColumnHashSet = new HashSet<String>();
        CostsColumnHashSet.add(KasebContract.Costs.COLUMN_COST_NAME);
        CostsColumnHashSet.add(KasebContract.Costs.COLUMN_COST_CODE);
        CostsColumnHashSet.add(KasebContract.Costs.COLUMN_AMOUNT);
        CostsColumnHashSet.add(KasebContract.Costs.COLUMN_COST_TYPE_ID);
        CostsColumnHashSet.add(KasebContract.Costs.COLUMN_DATE);
        CostsColumnHashSet.add(KasebContract.Costs.COLUMN_DESCRIPTION);

        int CostscolumnNameIndex = c.getColumnIndex("name");
        do {

            String CostscolumnName = c.getString(CostscolumnNameIndex);
            Log.d(LOG_TAG, CostscolumnName);
            CostsColumnHashSet.remove(CostscolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -Costs table- columns",
                CostsColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 5 -PaymentMethods table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.PaymentMethods.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -PaymentMethods table- information.",
                c.moveToFirst());

        final HashSet<String> PaymentMethodsColumnHashSet = new HashSet<String>();
        PaymentMethodsColumnHashSet.add(KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER);

        int PaymentMethodscolumnNameIndex = c.getColumnIndex("name");
        do {

            String PaymentMethodscolumnName = c.getString(PaymentMethodscolumnNameIndex);
            Log.d(LOG_TAG, PaymentMethodscolumnName);
            PaymentMethodsColumnHashSet.remove(PaymentMethodscolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -PaymentMethods table- columns",
                PaymentMethodsColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 6 -TaxTypes table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.TaxTypes.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -TaxTypes table- information.",
                c.moveToFirst());

        final HashSet<String> TaxTypesColumnHashSet = new HashSet<String>();
        TaxTypesColumnHashSet.add(KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER);

        int TaxTypescolumnNameIndex = c.getColumnIndex("name");
        do {

            String TaxTypescolumnName = c.getString(TaxTypescolumnNameIndex);
            Log.d(LOG_TAG, TaxTypescolumnName);
            TaxTypesColumnHashSet.remove(TaxTypescolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -TaxTypes table- columns",
                TaxTypesColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 7 -ProductHistory table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.ProductHistory.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -ProductHistory table- information.",
                c.moveToFirst());

        final HashSet<String> ProductHistoryColumnHashSet = new HashSet<String>();
        ProductHistoryColumnHashSet.add(KasebContract.ProductHistory.COLUMN_PRODUCT_ID);
        ProductHistoryColumnHashSet.add(KasebContract.ProductHistory.COLUMN_COST);
        ProductHistoryColumnHashSet.add(KasebContract.ProductHistory.COLUMN_QUANTITY);
        ProductHistoryColumnHashSet.add(KasebContract.ProductHistory.COLUMN_SALE_PRICE);
        ProductHistoryColumnHashSet.add(KasebContract.ProductHistory.COLUMN_DATE);

        int ProductHistorycolumnNameIndex = c.getColumnIndex("name");
        do {

            String ProductHistorycolumnName = c.getString(ProductHistorycolumnNameIndex);
            Log.d(LOG_TAG, ProductHistorycolumnName);
            ProductHistoryColumnHashSet.remove(ProductHistorycolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -ProductHistory table- columns",
                ProductHistoryColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 8 -DetailSaleTaxes table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.DetailSaleTaxes.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -DetailSaleTaxes table- information.",
                c.moveToFirst());

        final HashSet<String> DetailSaleTaxesColumnHashSet = new HashSet<String>();
        DetailSaleTaxesColumnHashSet.add(KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID);
        DetailSaleTaxesColumnHashSet.add(KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID);
        DetailSaleTaxesColumnHashSet.add(KasebContract.DetailSaleTaxes.COLUMN_AMOUNT);

        int DetailSaleTaxescolumnNameIndex = c.getColumnIndex("name");
        do {

            String DetailSaleTaxescolumnName = c.getString(DetailSaleTaxescolumnNameIndex);
            Log.d(LOG_TAG, DetailSaleTaxescolumnName);
            DetailSaleTaxesColumnHashSet.remove(DetailSaleTaxescolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -DetailSaleTaxes table- columns",
                DetailSaleTaxesColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 9 -DetailSaleProducts table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.DetailSaleProducts.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -DetailSaleProducts table- information.",
                c.moveToFirst());

        final HashSet<String> DetailSaleProductsColumnHashSet = new HashSet<String>();
        DetailSaleProductsColumnHashSet.add(KasebContract.DetailSaleProducts.COLUMN_DETAIL_SALE_ID);
        DetailSaleProductsColumnHashSet.add(KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID);
        DetailSaleProductsColumnHashSet.add(KasebContract.DetailSaleProducts.COLUMN_QUANTITY);
        DetailSaleProductsColumnHashSet.add(KasebContract.DetailSaleProducts.COLUMN_AMOUNT);

        int DetailSaleProductscolumnNameIndex = c.getColumnIndex("name");
        do {

            String DetailSaleProductscolumnName = c.getString(DetailSaleProductscolumnNameIndex);
            Log.d(LOG_TAG, DetailSaleProductscolumnName);
            DetailSaleProductsColumnHashSet.remove(DetailSaleProductscolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -DetailSaleProducts table- columns",
                DetailSaleProductsColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 10 -CostTypes table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.CostTypes.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -CostTypes table- information.",
                c.moveToFirst());

        final HashSet<String> CostTypesColumnHashSet = new HashSet<String>();
        CostTypesColumnHashSet.add(KasebContract.CostTypes.COLUMN_COST_TYPE_POINTER);

        int CostTypescolumnNameIndex = c.getColumnIndex("name");
        do {

            String CostTypescolumnName = c.getString(CostTypescolumnNameIndex);
            Log.d(LOG_TAG, CostTypescolumnName);
            CostTypesColumnHashSet.remove(CostTypescolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -CostTypes table- columns",
                CostTypesColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 11 -DetailSale table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.DetailSale.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -DetailSale table- information.",
                c.moveToFirst());

        final HashSet<String> DetailSaleColumnHashSet = new HashSet<String>();
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_SALE_ID);
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_DATE);
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_ITEMS_NUMBER);
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_SUB_TOTAL);
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_TOTAL_TAX);
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_TOTAL_DISCOUNT);
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_TOTAL_DUE);
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_TOTAL_PAID);
        DetailSaleColumnHashSet.add(KasebContract.DetailSale.COLUMN_IS_BALANCED);

        int DetailSalecolumnNameIndex = c.getColumnIndex("name");
        do {

            String DetailSalecolumnName = c.getString(DetailSalecolumnNameIndex);
            Log.d(LOG_TAG, DetailSalecolumnName);
            DetailSaleColumnHashSet.remove(DetailSalecolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -DetailSale table- columns",
                DetailSaleColumnHashSet.isEmpty());
        c.close();
        //endregion

        //  region 12 -DetailSalePayments table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.DetailSalePayments.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -DetailSalePayments table- information.",
                c.moveToFirst());

        final HashSet<String> DetailSalePaymentsColumnHashSet = new HashSet<String>();
        DetailSalePaymentsColumnHashSet.add(KasebContract.DetailSalePayments.COLUMN_DETAIL_SALE_ID);
        DetailSalePaymentsColumnHashSet.add(KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID);
        DetailSalePaymentsColumnHashSet.add(KasebContract.DetailSalePayments.COLUMN_DUE_DATE);
        DetailSalePaymentsColumnHashSet.add(KasebContract.DetailSalePayments.COLUMN_AMOUNT);

        int DetailSalePaymentscolumnNameIndex = c.getColumnIndex("name");
        do {

            String DetailSalePaymentscolumnName = c.getString(DetailSalePaymentscolumnNameIndex);
            Log.d(LOG_TAG, DetailSalePaymentscolumnName);
            DetailSalePaymentsColumnHashSet.remove(DetailSalePaymentscolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -DetailSalePayments table- columns",
                DetailSalePaymentsColumnHashSet.isEmpty());
        c.close();
        //endregion

        // region 13 -Customers table- columns?
        c = db.rawQuery("PRAGMA table_info(" + KasebContract.Customers.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for -Customers table- information.",
                c.moveToFirst());

        final HashSet<String> CustomersColumnHashSet = new HashSet<String>();
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_FIRST_NAME);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_LAST_NAME);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_BIRTHDAY);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_STATE_ID);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_PHONE_MOBILE);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_DESCRIPTION);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_IS_DELETED);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_EMAIL);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_PHONE_WORK);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_PHONE_OTHER);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_PHONE_FAX);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_ADDRESS_COUNTRY);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_ADDRESS_CITY);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_ADDRESS_STREET);
        CustomersColumnHashSet.add(KasebContract.Customers.COLUMN_ADDRESS_POSTAL_CODE);

        int CustomerscolumnNameIndex = c.getColumnIndex("name");
        do {

            String CustomerscolumnName = c.getString(CustomerscolumnNameIndex);
            Log.d(LOG_TAG, CustomerscolumnName);
            CustomersColumnHashSet.remove(CustomerscolumnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required -Customers table- columns",
                CustomersColumnHashSet.isEmpty());
        c.close();
        //endregion

        db.close();
    }

    //region Test tables
    //1
    public void testStateTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createStateValues();
        long StateRowId = db.insert(KasebContract.State.TABLE_NAME, null, testValues);
        assertTrue(StateRowId != -1);

        Cursor cursor = db.query(KasebContract.State.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -State table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -State table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.State.TABLE_NAME);

        assertFalse("Error: More than one record returned from -State table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //2
    public void testCostTypesTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCostTypesValues();
        long CostTypesRowId = db.insert(KasebContract.CostTypes.TABLE_NAME, null, testValues);
        assertTrue(CostTypesRowId != -1);

        Cursor cursor = db.query(KasebContract.CostTypes.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -CostTypes table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -CostTypes table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.CostTypes.TABLE_NAME);

        assertFalse("Error: More than one record returned from -CostTypes table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //3
    public void testCustomersTable() {
        long StateRowId = insertStateTable();
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCustomersValues(StateRowId);
        long CustomersRowId = db.insert(KasebContract.Customers.TABLE_NAME, null, testValues);
        assertTrue(CustomersRowId != -1);

        Cursor cursor = db.query(KasebContract.Customers.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -Customers table- query", cursor.moveToFirst());
        Log.d(LOG_TAG,"this is my log " + cursor.toString());
        TestUtilities.validateCurrentRecord("Error: -Customers table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.Customers.TABLE_NAME);

        assertFalse("Error: More than one record returned from -Customers table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //4
    public void testCostTable() {
        long CostTypesRowId = insertCostTypesTable();
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCostsValues(CostTypesRowId);
        long CostsRowId = db.insert(KasebContract.Costs.TABLE_NAME, null, testValues);
        assertTrue(CostsRowId != -1);

        Cursor cursor = db.query(KasebContract.Costs.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -Costs table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -Costs table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.Costs.TABLE_NAME);

        assertFalse("Error: More than one record returned from -Costs table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //5
    public void testPaymentMethodsTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createPaymentMethodsValues();
        long PaymentMethodsRowId = db.insert(KasebContract.PaymentMethods.TABLE_NAME, null, testValues);
        assertTrue(PaymentMethodsRowId != -1);

        Cursor cursor = db.query(KasebContract.PaymentMethods.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -PaymentMethods table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -PaymentMethods table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.PaymentMethods.TABLE_NAME);

        assertFalse("Error: More than one record returned from -PaymentMethods table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //6
    public void testTaxTypesTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTaxTypesValues();
        long TaxTypesRowId = db.insert(KasebContract.TaxTypes.TABLE_NAME, null, testValues);
        assertTrue(TaxTypesRowId != -1);

        Cursor cursor = db.query(KasebContract.TaxTypes.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -TaxTypes table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -TaxTypes table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.TaxTypes.TABLE_NAME);

        assertFalse("Error: More than one record returned from -TaxTypes table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //7
    public void testProductsTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createProductsValues();
        long ProductsRowId = db.insert(KasebContract.Products.TABLE_NAME, null, testValues);
        assertTrue(ProductsRowId != -1);

        Cursor cursor = db.query(KasebContract.Products.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -Products table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -Products table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.Products.TABLE_NAME);

        assertFalse("Error: More than one record returned from -Products table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //8
    public void testSalesTable() {
        long StateRowId = insertStateTable();
        long CustomersRowId = insertCustomersTable(StateRowId);
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createSalesValues(CustomersRowId);
        long SalesRowId = db.insert(KasebContract.Sales.TABLE_NAME, null, testValues);
        assertTrue(SalesRowId != -1);

        Cursor cursor = db.query(KasebContract.Sales.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -Sales table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -Sales table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.Sales.TABLE_NAME);

        assertFalse("Error: More than one record returned from -Sales table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //9
    public void testDetailSaleTable() {
        long stateRowId = insertStateTable();
        long customersRowId = insertCustomersTable(stateRowId);
        long salesRowId = insertSalesTable(customersRowId);
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createDetailSaleValues(salesRowId);
        long detailSaleRowId = db.insert(KasebContract.DetailSale.TABLE_NAME, null, testValues);
        assertTrue(detailSaleRowId != -1);

        Cursor cursor = db.query(KasebContract.DetailSale.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -DetailSale table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -DetailSale table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.DetailSale.TABLE_NAME);

        assertFalse("Error: More than one record returned from -DetailSale table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //10
    public void testDetailSalePaymentsTable() {
        long StateRowId = insertStateTable();
        long CustomersRowId = insertCustomersTable(StateRowId);
        long SalesRowId = insertSalesTable(CustomersRowId);
        long DetailSaleRowId = insertDetailSaleTable(SalesRowId);
        long PaymentMethodsRowId = insertPaymentMethodsTable();
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createDetailSalePaymentsValues(DetailSaleRowId, PaymentMethodsRowId);
        long DetailSalePaymentsRowId = db.insert(KasebContract.DetailSalePayments.TABLE_NAME, null, testValues);
        assertTrue(DetailSalePaymentsRowId != -1);

        Cursor cursor = db.query(KasebContract.DetailSalePayments.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -DetailSalePayments table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -DetailSalePayments table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.DetailSalePayments.TABLE_NAME);

        assertFalse("Error: More than one record returned from -DetailSalePayments table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //11
    public void testProductHistoryTable() {
        long ProductsRowId = insertProductsTable();
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createProductHistoryValues(ProductsRowId);
        long ProductHistoryRowId = db.insert(KasebContract.ProductHistory.TABLE_NAME, null, testValues);
        assertTrue(ProductHistoryRowId != -1);

        Cursor cursor = db.query(KasebContract.ProductHistory.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -ProductHistory table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -ProductHistory table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.ProductHistory.TABLE_NAME);

        assertFalse("Error: More than one record returned from -ProductHistory table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //12
    public void testDetailSaleTaxesTable() {

        long StateRowId = insertStateTable();
        long CustomersRowId = insertCustomersTable(StateRowId);
        long SalesRowId = insertSalesTable(CustomersRowId);
        long DetailSaleRowId = insertDetailSaleTable(SalesRowId);
        long TaxTypesRowId = insertTaxTypesTable();

        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createDetailSaleTaxesValues(DetailSaleRowId, TaxTypesRowId);
        long DetailSaleTaxesRowId = db.insert(KasebContract.DetailSaleTaxes.TABLE_NAME, null, testValues);
        assertTrue(DetailSaleTaxesRowId != -1);

        Cursor cursor = db.query(KasebContract.DetailSaleTaxes.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -DetailSaleTaxes table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -DetailSaleTaxes table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.DetailSaleTaxes.TABLE_NAME);

        assertFalse("Error: More than one record returned from -DetailSaleTaxes table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //13
    public void testDetailSaleProductsTable() {

        long StateRowId = insertStateTable();
        long CustomersRowId = insertCustomersTable(StateRowId);
        long SalesRowId = insertSalesTable(CustomersRowId);
        long DetailSaleRowId = insertDetailSaleTable(SalesRowId);
        long ProductsRowId = insertProductsTable();

        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createDetailSaleProductsValues(ProductsRowId, DetailSaleRowId);
        long DetailSaleProductsRowId = db.insert(KasebContract.DetailSaleProducts.TABLE_NAME, null, testValues);
        assertTrue(DetailSaleProductsRowId != -1);

        Cursor cursor = db.query(KasebContract.DetailSaleProducts.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Records returned from -DetailSaleProducts table- query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: -DetailSaleProducts table- Query Validation Failed"
                , cursor
                , testValues
                , KasebContract.DetailSaleProducts.TABLE_NAME);

        assertFalse("Error: More than one record returned from -DetailSaleProducts table- query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }
    //endregion

    //region Insert to tables
    //1
    public long insertStateTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createStateValues();
        long StateRowId = db.insert(KasebContract.State.TABLE_NAME, null, testValues);
        return StateRowId;
    }

    //2
    public long insertCostTypesTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCostTypesValues();
        long CostTypesRowId = db.insert(KasebContract.CostTypes.TABLE_NAME, null, testValues);
        return CostTypesRowId;
    }

    //3
    public long insertPaymentMethodsTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createPaymentMethodsValues();
        long PaymentMethodsRowId = db.insert(KasebContract.PaymentMethods.TABLE_NAME, null, testValues);
        return PaymentMethodsRowId;
    }

    //4
    public long insertTaxTypesTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTaxTypesValues();
        long TaxTypesRowId = db.insert(KasebContract.TaxTypes.TABLE_NAME, null, testValues);
        return TaxTypesRowId;
    }

    //5
    public long insertProductsTable() {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createProductsValues();
        long ProductsRowId = db.insert(KasebContract.Products.TABLE_NAME, null, testValues);
        return ProductsRowId;
    }

    //6
    public long insertSalesTable(long CustomersRowId) {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createSalesValues(CustomersRowId);
        long SalesRowId = db.insert(KasebContract.Sales.TABLE_NAME, null, testValues);
        return SalesRowId;
    }

    //7
    public long insertCustomersTable(long StateRowId) {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCustomersValues(StateRowId);
        long CustomersRowId = db.insert(KasebContract.Customers.TABLE_NAME, null, testValues);
        return CustomersRowId;
    }

    //8
    public long insertDetailSaleTable(long SalesRowId) {
        KasebDbHelper dbHelper = new KasebDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createDetailSaleValues(SalesRowId);
        long DetailSaleRowId = db.insert(KasebContract.DetailSale.TABLE_NAME, null, testValues);
        return DetailSaleRowId;
    }
    //endregion
}






























