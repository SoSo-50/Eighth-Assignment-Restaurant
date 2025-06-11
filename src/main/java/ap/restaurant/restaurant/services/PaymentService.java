package ap.restaurant.restaurant.services;

import ap.restaurant.restaurant.dao.PaymentDAO;
import ap.restaurant.restaurant.dao.OrderDAO;
import ap.restaurant.restaurant.models.Payment;
import ap.restaurant.restaurant.models.Payment.PaymentStatus;
import ap.restaurant.restaurant.utils.SessionManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PaymentService {

    public static PaymentStatus processPayment(int orderId, double amount, String paymentMethod)
            throws SQLException, IllegalArgumentException {

        if (amount <= 0) {
            throw new IllegalArgumentException("مبلغ پرداخت باید بیشتر از صفر باشد");
        }

        if (!OrderDAO.orderExists(orderId)) {
            throw new IllegalArgumentException("سفارش مورد نظر یافت نشد");
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);

        boolean paymentSaved = PaymentDAO.createPayment(payment);

        if (paymentSaved) {
            boolean orderUpdated = OrderDAO.updateOrderStatus(orderId, "PAID");

            if (orderUpdated) {
                payment.setStatus(PaymentStatus.SUCCESS);
                PaymentDAO.updatePaymentStatus(payment.getId(), PaymentStatus.SUCCESS);
                return PaymentStatus.SUCCESS;
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                PaymentDAO.updatePaymentStatus(payment.getId(), PaymentStatus.FAILED);
                return PaymentStatus.FAILED;
            }
        }

        return PaymentStatus.FAILED;
    }

    public static boolean refundPayment(int paymentId)
            throws SQLException, IllegalArgumentException {

        Payment payment = PaymentDAO.getPaymentById(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("پرداخت مورد نظر یافت نشد");
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalStateException("فقط پرداخت‌های موفق قابل استرداد هستند");
        }

        boolean refunded = PaymentDAO.updatePaymentStatus(paymentId, PaymentStatus.REFUNDED);

        if (refunded) {
            return OrderDAO.updateOrderStatus(payment.getOrderId(), "REFUNDED");
        }

        return false;
    }

    public static PaymentStatus checkPaymentStatus(int orderId) throws SQLException {
        return PaymentDAO.getPaymentStatusByOrder(orderId);
    }

    public static Payment getPaymentDetails(int orderId) throws SQLException {
        return PaymentDAO.getPaymentByOrder(orderId);
    }
}