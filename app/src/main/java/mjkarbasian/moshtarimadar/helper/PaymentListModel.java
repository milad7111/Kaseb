package mjkarbasian.moshtarimadar.helper;

/**
 * Created by Unique on 01/12/2016.
 */
public class PaymentListModel {

    private Long paymentAmount = 0l;
    private String paymentDueDate = "";
    private String paymentMethod = "";

    public Long getpaymentAmount() {
        return this.paymentAmount;
    }

    public void setpaymentAmount(Long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getpaymentDueDate() {
        return this.paymentDueDate;
    }

    public void setpaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getpaymentMethod() {
        return this.paymentMethod;
    }

    public void setpaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
