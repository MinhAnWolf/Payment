package com.demo.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
  private String partnerCode;
  private String partnerName;
  private String storeId;
  private String requestType;
  private String ipnUrl;
  private String redirectUrl;
  private String orderId;
  private long amount;
  private String orderInfo;
  private String requestId;
  private String extraData;
  private String signature;
  private String lang = "vi";
  private Integer orderExpireTime;
}
