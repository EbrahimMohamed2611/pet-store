package eg.gov.iti.jets.petstore.services;

import com.stripe.exception.StripeException;

import java.util.Map;

public interface PaymentService {

    public Map<String, String> createPaymentSession(Long userId) throws StripeException;

}
