package mjkarbasian.moshtarimadar.helper;

/**
 * Created by Unique on 01/12/2016.
 */
public class TaxListModel {

    private Long taxAmount = 0l;
    private String taxPercent = "";
    private String taxType = "";

    public Long getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(Long taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxPercent() {
        return this.taxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        this.taxPercent = taxPercent;
    }

    public String getTaxType() {
        return this.taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }
}
