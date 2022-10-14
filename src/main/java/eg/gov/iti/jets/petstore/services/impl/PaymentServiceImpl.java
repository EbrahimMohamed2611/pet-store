package eg.gov.iti.jets.petstore.services.impl;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import eg.gov.iti.jets.petstore.entities.CartItem;
import eg.gov.iti.jets.petstore.entities.Customer;
import eg.gov.iti.jets.petstore.repositories.CustomerRepository;
import eg.gov.iti.jets.petstore.repositories.ProductRepository;
import eg.gov.iti.jets.petstore.services.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final String domain;
    private final String apiKey;

    public PaymentServiceImpl(ModelMapper modelMapper, ProductRepository productRepository, CustomerRepository customerRepository, @Value("${payment.domain}") String domain, @Value("${payment.api.key}") String apiKey) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.domain = domain;
        this.apiKey = apiKey;
    }

    @Override
    public Map<String, String> createPaymentSession(Long userId) throws StripeException {

        Customer customer = customerRepository.getById(userId);
        Set<CartItem> shoppingCart = customer.getShoppingCart();

        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> lineItems = shoppingCart.stream().map(
                item-> SessionCreateParams.LineItem.builder()
                        .setQuantity(item.getQuantity().longValue())
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("egp")
                                        .setUnitAmount(item.getProduct().getPrice().longValue()*100)
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build())
                                        .build())
                        .build()
        ).collect(Collectors.toList());

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setCustomerEmail(customer.getEmail())
                        .setSubmitType(SessionCreateParams.SubmitType.PAY)
//                        .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
//                        .setShippingAddressCollection(
//                                SessionCreateParams.ShippingAddressCollection.builder()
//                                        .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA)
//                                        .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
//                                        .build())
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(domain + "/success")
                        .setCancelUrl(domain + "/failed").addAllLineItem(lineItems).build();

        Session session = Session.create(params);
        HashMap<String, String> responseData = new HashMap<String, String>();
        responseData.put("id", session.getId());
        return responseData;
    }
}
