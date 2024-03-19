package com.example.summerproject.utils;

import com.example.summerproject.exception.NotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Component
public class EsewaUtil {

    public String Esewahash( String total,String uuid){
        try {
            String secret = "8gBm/:&EnhH.1/q";
            String message = "total_amount="+total+",transaction_uuid="+uuid+",product_code=EPAYTEST";
            Mac sha256_HMAC = null;
            try {
                sha256_HMAC = Mac.getInstance("HmacSHA256");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(),"HmacSHA256");
            try {
                sha256_HMAC.init(secret_key);
            } catch (InvalidKeyException ex) {
                throw new RuntimeException(ex);
            }
            String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
           return hash;
        }
        catch (Exception e){
            throw new NotFoundException();
        }
    }

    public String generateTransactionUuid(){
        return String.valueOf(UUID.randomUUID());
    }
    public String calculateTax(String amount) {
        return String.valueOf(Double.parseDouble(amount)*0.13);
    }

    public String calculateServiceCharge(String amount) {
        return String.valueOf(Double.parseDouble(amount) *0.10 );
    }
    public String calculateDeliveryCharge(String amount) {
        return String.valueOf(Double.parseDouble(amount) *0.5);
    }

    public String calculateTotal(String amount) {

        Double total=Double.parseDouble(amount)
                +Double.parseDouble(amount)*0.13
                +Double.parseDouble(amount)*0.10
                +Double.parseDouble(amount)*0.05;
        return String.valueOf(total);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
