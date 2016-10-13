package mjkarbasian.moshtarimadar.Data;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;
import java.util.List;

/**
 * Created by Unique on 10/7/2016.
 */
public class TestKasebContract extends AndroidTestCase {

    public static long sample_number = 100;
    public static String sample_text = "sample100";

    private static final String LOG_TAG = TestKasebContract.class.getSimpleName();

    public void testBuildKasebUri() {
        //region 1
        Uri stateUri = KasebContract.State.buildStateUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + stateUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildStateID in " +
                        "KasebContract.",
                stateUri);

        List<String> stateUriPaths = stateUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), stateUriPaths.get(0));
        assertEquals("State sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , stateUri.getLastPathSegment());
        //endregion

        //region 2
        Uri costTypesUri = KasebContract.CostTypes.buildCostTypesUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + costTypesUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildCostTypesID in " +
                        "KasebContract.",
                costTypesUri);

        List<String> costTypesUriPaths = costTypesUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), costTypesUriPaths.get(0));
        assertEquals("CostTypes sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , costTypesUri.getLastPathSegment());
        //endregion

        //region 3
        Uri customersUri = KasebContract.Customers.buildCustomerUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + customersUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildCustomersID in " +
                        "KasebContract.",
                customersUri);

        List<String> customersUriPaths = customersUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), customersUriPaths.get(0));
        assertEquals("Customers sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , customersUri.getLastPathSegment());
        //endregion

        //region 4
        Uri costUri = KasebContract.Costs.buildCostsUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + costUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildCostsID in " +
                        "KasebContract.",
                costUri);

        List<String> costUriPaths = costUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), costUriPaths.get(0));
        assertEquals("Costs sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , costUri.getLastPathSegment());
        //endregion

        //region 5
        Uri paymentMethodsUri = KasebContract.PaymentMethods.buildPaymentMethodsUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + paymentMethodsUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildPaymentMethodsID in " +
                        "KasebContract.",
                paymentMethodsUri);

        List<String> paymentMethodsUriPaths = paymentMethodsUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), paymentMethodsUriPaths.get(0));
        assertEquals("PaymentMethods sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , paymentMethodsUri.getLastPathSegment());
        //endregion

        //region 6
        Uri taxTypesUri = KasebContract.TaxTypes.buildTaxTypesUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + taxTypesUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildTaxTypesID in " +
                        "KasebContract.",
                taxTypesUri);

        List<String> taxTypesUriPaths = taxTypesUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), taxTypesUriPaths.get(0));
        assertEquals("TaxTypes sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , taxTypesUri.getLastPathSegment());
        //endregion

        //region 7
        Uri productsUri = KasebContract.Products.buildProductsUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + productsUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildProductsID in " +
                        "KasebContract.",
                productsUri);

        List<String> productsUriPaths = productsUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), productsUriPaths.get(0));
        assertEquals("Products sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , productsUri.getLastPathSegment());
        //endregion

        //region 8
        Uri salesUri = KasebContract.Sales.buildSalesUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + salesUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildSalesID in " +
                        "KasebContract.",
                salesUri);

        List<String> salesUriPaths = salesUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), salesUriPaths.get(0));
        assertEquals("Sales sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , salesUri.getLastPathSegment());
        //endregion

        //region 9
        Uri detailSaleUri = KasebContract.DetailSale.buildDetailSaleUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + detailSaleUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildDetailSaleID in " +
                        "KasebContract.",
                detailSaleUri);

        List<String> detailSaleUriPaths = detailSaleUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), detailSaleUriPaths.get(0));
        assertEquals("DetailSale sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , detailSaleUri.getLastPathSegment());
        //endregion

        //region 10
        Uri detailSalePaymentsUri = KasebContract.DetailSalePayments.buildDetailSalePaymentsUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + detailSalePaymentsUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildDetailSalePaymentsID in " +
                        "KasebContract.",
                detailSalePaymentsUri);

        List<String> detailSalePaymentsUriPaths = detailSalePaymentsUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), detailSalePaymentsUriPaths.get(0));
        assertEquals("DetailSalePayments sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , detailSalePaymentsUri.getLastPathSegment());
        //endregion

        //region 11
        Uri productHistoryUri = KasebContract.ProductHistory.buildProductHistoryUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + productHistoryUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildProductHistoryID in " +
                        "KasebContract.",
                productHistoryUri);

        List<String> productHistoryUriPaths = productHistoryUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), productHistoryUriPaths.get(0));
        assertEquals("ProductHistory sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , productHistoryUri.getLastPathSegment());
        //endregion

        //region 12
        Uri detailSaleTaxesUri = KasebContract.DetailSaleTaxes.buildDetailSaleTaxesUri(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + detailSaleTaxesUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildDetailSaleTaxesID in " +
                        "KasebContract.",
                detailSaleTaxesUri);

        List<String> detailSaleTaxesUriPaths = detailSaleTaxesUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), detailSaleTaxesUriPaths.get(0));
        assertEquals("DetailSaleTaxes sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , detailSaleTaxesUri.getLastPathSegment());
        //endregion

        //region 13
        Uri detailSaleProductsUri = KasebContract.DetailSaleProducts.buildDetailSaleProducts(sample_number++);
        Log.d(LOG_TAG, "created uri is: " + detailSaleProductsUri.toString());
        assertNotNull("Error: Null Uri returned.  You must fill-in buildDetailSaleProductsID in " +
                        "KasebContract.",
                detailSaleProductsUri);

        List<String> detailSaleProductsUriPaths = detailSaleProductsUri.getPathSegments();
        assertEquals("Error: Kaseb  not properly appended to the end of the Uri",
                sample_text + (sample_number++), detailSaleProductsUriPaths.get(0));
        assertEquals("DetailSaleProducts sort order not properly appended to the end of the Uri", sample_text + (sample_number++)
                , detailSaleProductsUri.getLastPathSegment());
        //endregion
    }
}
