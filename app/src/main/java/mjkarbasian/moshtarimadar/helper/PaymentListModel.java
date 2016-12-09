package mjkarbasian.moshtarimadar.helper;

/**
 * Created by Unique on 01/12/2016.
 */
public class PaymentListModel {

    private Long paymentAmount = 0l;
    private String paymentDueDate = "";
    private String paymentMethod = "";

    public Long getPaymentAmount() {
        return this.paymentAmount;
    }

    public void setPaymentAmount(Long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDueDate() {
        return this.paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
