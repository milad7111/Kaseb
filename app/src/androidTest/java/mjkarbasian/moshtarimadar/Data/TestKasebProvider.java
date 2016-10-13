package mjkarbasian.moshtarimadar.Data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import mjkarbasian.moshtarimadar.Tool.TestUtilities;

import static mjkarbasian.moshtarimadar.Data.KasebContract.*;

/**
 * Created by Unique on 10/13/2016.
 */
public class TestKasebProvider extends AndroidTestCase {

    public static long sample_number = 100;
    final static String LOG_TAG = TestKasebProvider.class.getSimpleName();
    KasebProvider ojectKasebProvider = new KasebProvider();

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        deleteAllRecords();
//    }

    private void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    private void deleteAllRecordsFromProvider() {

//        mContext.getContentResolver().delete(
//                State.buildStateUri(100),
//                null,
//                null
//        );

        Cursor cursor = mContext.getContentResolver().query(
                State.buildStateUri(100),
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from -State table- during delete", 0, cursor.getCount());
        cursor.close();

        cursor.close();
    }

    public void testGetType() {

        //region 1 State
        Long State_id = sample_number++;
        Uri g = KasebContract.State.buildStateUri(State_id);
        String stateType = ojectKasebProvider.getType(KasebContract.State.buildStateUri(State_id));
        assertEquals("Error: StateUri type was not correct", KasebContract.State.CONTENT_ITEM_TYPE, stateType);
        //endregion 1

        //region 2 CostTypes
        Long CostTypes_id = sample_number++;
        Uri d = KasebContract.CostTypes.buildCostTypesUri(CostTypes_id);
        String costTypesType = ojectKasebProvider.getType(KasebContract.CostTypes.buildCostTypesUri(CostTypes_id));
        assertEquals("Error: CostTypesUri type was not correct", KasebContract.CostTypes.CONTENT_ITEM_TYPE, costTypesType);
        //endregion 3

        //region 3 Customers
        Long customers_id = sample_number++;
        String customersType = ojectKasebProvider.getType(KasebContract.Customers.buildCustomerUri(customers_id));
        assertEquals("Error: CustomersUri type was not correct", KasebContract.Customers.CONTENT_ITEM_TYPE, customersType);
        //endregion 3

        //region 4 Costs
        Long costs_id = sample_number++;
        String CostsType = ojectKasebProvider.getType(KasebContract.Costs.buildCostsUri(costs_id));
        assertEquals("Error: CostsUri type was not correct", KasebContract.Costs.CONTENT_ITEM_TYPE, CostsType);
        //endregion 4

        //region 5 PaymentMethods
        Long paymentMethods_id = sample_number++;
        String PaymentMethodsType = ojectKasebProvider.getType(KasebContract.PaymentMethods.buildPaymentMethodsUri(paymentMethods_id));
        assertEquals("Error: PaymentMethodsUri type was not correct", KasebContract.PaymentMethods.CONTENT_ITEM_TYPE, PaymentMethodsType);
        //endregion 5

        //region 6 TaxTypes
        Long TaxTypes_id = sample_number++;
        String taxTypesType = ojectKasebProvider.getType(KasebContract.TaxTypes.buildTaxTypesUri(TaxTypes_id));
        assertEquals("Error: TaxTypesUri type was not correct", KasebContract.TaxTypes.CONTENT_ITEM_TYPE, taxTypesType);
        //endregion 6

        //region 7 Products
        Long Products_id = sample_number++;
        String productsType = ojectKasebProvider.getType(KasebContract.Products.buildProductsUri(Products_id));
        assertEquals("Error: ProductsUri type was not correct", KasebContract.Products.CONTENT_ITEM_TYPE, productsType);
        //endregion 7

        //region 8 Sales
        Long Sales_id = sample_number++;
        String salesType = ojectKasebProvider.getType(KasebContract.Sales.buildSalesUri(Sales_id));
        assertEquals("Error: SalesUri type was not correct", KasebContract.Sales.CONTENT_ITEM_TYPE, salesType);
        //endregion 8

        //region 9 DetailSale
        Long DetailSale_id = sample_number++;
        String detailSaleType = ojectKasebProvider.getType(KasebContract.DetailSale.buildDetailSaleUri(DetailSale_id));
        assertEquals("Error: DetailSaleUri type was not correct", KasebContract.DetailSale.CONTENT_ITEM_TYPE, detailSaleType);
        //endregion 9

        //region 10 DetailSalePayments
        Long DetailSalePayments_id = sample_number++;
        String detailSalePaymentsType = ojectKasebProvider.getType(KasebContract.DetailSalePayments.buildDetailSalePaymentsUri(DetailSalePayments_id));
        assertEquals("Error: DetailSalePaymentsUri type was not correct", KasebContract.DetailSalePayments.CONTENT_ITEM_TYPE, detailSalePaymentsType);
        //endregion 10

        //region 11 ProductHistory
        Long ProductHistory_id = sample_number++;
        String productHistoryType = ojectKasebProvider.getType(KasebContract.ProductHistory.buildProductHistoryUri(ProductHistory_id));
        assertEquals("Error: ProductHistoryUri type was not correct", KasebContract.ProductHistory.CONTENT_ITEM_TYPE, productHistoryType);
        //endregion 11

        //region 12 DetailSaleTaxes
        Long DetailSaleTaxes_id = sample_number++;
        String detailSaleTaxesType = ojectKasebProvider.getType(KasebContract.DetailSaleTaxes.buildDetailSaleTaxesUri(DetailSaleTaxes_id));
        assertEquals("Error: DetailSaleTaxesUri type was not correct", KasebContract.DetailSaleTaxes.CONTENT_ITEM_TYPE, detailSaleTaxesType);
        //endregion 12

        //region 13 DetailSaleProducts
        Long DetailSaleProducts_id = sample_number++;
        String detailSaleProductsType = ojectKasebProvider.getType(KasebContract.DetailSaleProducts.buildDetailSaleProducts(DetailSaleProducts_id));
        assertEquals("Error: DetailSaleProductsUri type was not correct", KasebContract.DetailSaleProducts.CONTENT_ITEM_TYPE, detailSaleProductsType);
        //endregion 13
    }

    public void testBasicQuery() {

        KasebDbHelper kasebDbHelper = new KasebDbHelper(getContext());
        SQLiteDatabase db = kasebDbHelper.getWritableDatabase();
        ContentValues CostTypesValues = TestUtilities.createCostTypesValues();
        long CostTypesId = db.insert(CostTypes.TABLE_NAME, null, CostTypesValues);
        assertTrue("Unable to insert CostTypes row", CostTypesId != -1);

        ContentValues CostsValues = TestUtilities.createCostsValues(CostTypesId);
        long CostsId = db.insert(Costs.TABLE_NAME, null, CostsValues);
        assertTrue("Unable to insert Movie row", CostsId != -1);

        db.close();

        // Test the basic content provider query
        Cursor CostTypesCursor = mContext.getContentResolver().query(
                CostTypes.buildCostTypesUri(100),
                null,
                null,
                null,
                null
        );
//        assertNotNull("Cursor is null", CostTypesCursor);

//        CostTypesCursor.close();

        Cursor theCosts = mContext.getContentResolver().query(
                Costs.buildCostsUri(100),
                null,
                null,
                null,
                null
        );
//        assertNotNull("Cursor is null", theCosts);
//        theCosts.close();
//    }

//    public void testProviderRegistry() {
//        PackageManager pm = mContext.getPackageManager();
//
//        // We define the component name based on the package name from the context and the
//        // WeatherProvider class.
//        ComponentName componentName = new ComponentName(mContext.getPackageName(),
//                KasebProvider.class.getName());
//
//        try {
//            // Fetch the provider info using the component name from the PackageManager
//            // This throws an exception if the provider isn't registered.
//            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
//
//            // Make sure that the registered authority matches the authority from the Contract.
//            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
//                            " instead of authority: " + CONTENT_AUTHORITY,
//                    providerInfo.authority, CONTENT_AUTHORITY);
//
//        } catch (PackageManager.NameNotFoundException e) {
//            // I guess the provider isn't registered correctly.
//            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
//                    false);
//        }
//    }
    }
}