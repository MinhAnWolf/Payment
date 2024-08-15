package com.demo.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
  public ResponseEntity<?> paymentMomo(@RequestBody Request requestClient) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
    ObjectMapper objectMapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    String createPayment = "https://test-payment.momo.vn/v2/gateway/api/create";
    String requestJson = null;
    requestClient.setSignature(signature(requestClient));
    try {
      requestJson = objectMapper.writeValueAsString(requestClient);
    } catch (JsonProcessingException e) {
      System.out.println(e);
      throw new RuntimeException(e);
    }
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
    ResponseEntity<String> response = restTemplate.exchange(createPayment, HttpMethod.POST, request, String.class);
    return ResponseEntity.ok(response.getBody());
  }

  private String signature(Request request) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
    String accessKey = "F8BBA842ECF85";
    String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";

    String requestRawData = new StringBuilder()
        .append(Parameter.ACCESS_KEY).append("=").append(accessKey).append("&")
        .append(Parameter.AMOUNT).append("=").append(request.getAmount()).append("&")
        .append(Parameter.EXTRA_DATA).append("=").append(request.getExtraData()).append("&")
        .append(Parameter.IPN_URL).append("=").append(request.getIpnUrl()).append("&")
        .append(Parameter.ORDER_ID).append("=").append(request.getOrderId()).append("&")
        .append(Parameter.ORDER_INFO).append("=").append(request.getOrderInfo()).append("&")
        .append(Parameter.PARTNER_CODE).append("=").append(request.getPartnerCode()).append("&")
        .append(Parameter.REDIRECT_URL).append("=").append(request.getRedirectUrl()).append("&")
        .append(Parameter.REQUEST_ID).append("=").append(request.getRequestId()).append("&")
        .append(Parameter.REQUEST_TYPE).append("=").append(request.getRequestType())
        .toString();

    return Encoder.signHmacSHA256(requestRawData, secretKey);
  }

  // document
  // https://developers.momo.vn/v3/vi/docs/payment/api/wallet/onetime#l%E1%BA%A5y-ph%C6%B0%C6%A1ng-th%E1%BB%A9c-thanh-to%C3%A1n
  // https://github.com/momo-wallet/java/blob/master/src/main/java/com/mservice/processor/CreateOrderMoMo.java#L57
  // https://developers.momo.vn/v2/#/docs/aiov2/


  public class Parameter {
    public static String PARTNER_CODE = "partnerCode";
    public static String PARTNER_CLIENT_ID = "partnerClientId";
    public static String CALLBACK_TOKEN = "callbackToken";
    public static String DESCRIPTION = "description";
    public static String ACCESS_KEY = "accessKey";
    public static String REQUEST_ID = "requestId";
    public static String AMOUNT = "amount";
    public static String ORDER_ID = "orderId";
    public static String ORDER_INFO = "orderInfo";
    public static String REQUEST_TYPE = "requestType";
    public static String EXTRA_DATA = "extraData";
    public static String MESSAGE = "message";
    public static String PAY_URL = "payUrl";
    public static String RESULT_CODE = "resultCode";
    public static String REDIRECT_URL = "redirectUrl";
    public static String IPN_URL = "ipnUrl";
    public static String TOKEN = "token";
    public static String TRANS_ID = "transId";
  }

  public static class Encoder {
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static String toHexString(byte[] bytes) {
      StringBuilder sb = new StringBuilder(bytes.length * 2);
      Formatter formatter = new Formatter(sb);
      for (byte b : bytes) {
        formatter.format("%02x", b);
      }
      return sb.toString();
    }
    public static String signHmacSHA256(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA256);
      Mac mac = Mac.getInstance(HMAC_SHA256);
      mac.init(secretKeySpec);
      byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return toHexString(rawHmac);
    }
  }
}
