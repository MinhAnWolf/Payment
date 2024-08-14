package com.demo.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/payment")
public class PaymentController {
  //https://developers.momo.vn/v3/vi/docs/payment/api/atm/onetime
  @PostMapping("/momo")
  public ResponseEntity<?> paymentMomo(@RequestBody Request requestClient) {
    ObjectMapper objectMapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    String createPayment = "https://test-payment.momo.vn/v2/gateway/api/create";
    String requestJson = null;
    try {
      requestJson = objectMapper.writeValueAsString(requestClient);
    } catch (JsonProcessingException e) {
      System.out.println(e);
      throw new RuntimeException(e);
    }
    System.out.println(requestJson);
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
    ResponseEntity<String> response = restTemplate.exchange(createPayment, HttpMethod.POST, request, String.class);
    return ResponseEntity.ok(response.getBody());
  }
}
