package com.mukesh.payment.gateway;

import com.mukesh.payment.entity.PaymentEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FakePaymentGateway implements PaymentGateway{


    @Override
    public boolean process(PaymentEntity entity){
        log.info("Processing payment {} for amount {}",
                payment.getPaymentId(),
                payment.getAmount());

        return true;
    }


}
