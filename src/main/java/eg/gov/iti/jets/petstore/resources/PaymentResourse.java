package eg.gov.iti.jets.petstore.resources;


import com.stripe.exception.StripeException;
import eg.gov.iti.jets.petstore.services.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("customers")
public class PaymentResourse {

    private final PaymentService paymentService;

    public PaymentResourse(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping("{id}/payment")
    /**
     * Payment with Stripe checkout page
     *
     * @throws StripeException
     */
    public Map<String, String> paymentWithCheckoutPage(@PathVariable Long id) throws StripeException {

        return paymentService.createPaymentSession(id);
    }

}
