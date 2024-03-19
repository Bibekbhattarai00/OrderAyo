package com.example.summerproject.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EsewaRequestDto {

    @JsonProperty("amount")
    private String amount;
    @JsonProperty("failure_url")
    private String failureUrl;
    @JsonProperty("product_delivery_charge")
    private String productDeliveryCharge;
    @JsonProperty("product_service_charge")
    private String productServiceCharge;
    @JsonProperty("product_code")
    private String productCode;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("signed_field_names")
    private String signedFieldNames;
    @JsonProperty("success_url")
    private String successUrl;
    @JsonProperty("tax_amount")
    private String taxAmount;
    @JsonProperty("total_amount")
    private String totalAmount;
    @JsonProperty("transaction_uuid")
    private String transactionUuid;

}