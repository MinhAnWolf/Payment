
# Payment with momo

This is project is small about payment with momo

EVN:
+ JDK 17
## API Reference

#### Create payment

```http
  POST http://localhost:8080/payment/momo
```

``` 
Request:
{
  "partnerCode": "MOMO",
  "partnerName" : "Test",
  "storeId" : "MoMoTestStore",
  "requestType": "payWithMethod",
  "ipnUrl": "https://momo.vn",
  "redirectUrl": "https://momo.vn",
  "orderId": "MM1540456472575123",
  "amount": 150000,
  "lang":  "vi",
  "orderInfo": "SDK team.",
  "orderExpireTime" : 30,
  "requestId": "MM1540456472575123",
  "extraData": ""
}

```

``` 
Response success:
    {
    "partnerCode": "MOMO",
    "orderId": "MM1540456472575123",
    "requestId": "MM1540456472575123",
    "amount": 150000,
    "responseTime": 1723703674830,
    "message": "Thành công.",
    "resultCode": 0,
    "payUrl": "https://test-payment.momo.vn/v2/gateway/pay?t=TU9NT3xNTTE1NDA0NTY0NzI1NzUxMjM&s=fa392b57afb6ef71c870d4f60d9f5fd79b11807e99389de5afe201ac5d365ddf",
    "shortLink": "https://test-payment.momo.vn/shortlink/l9VWkM8X1U"
}
```
## Note

Seft handle ***Signature*** because momo api not support create fit

Working Signature:
![image](https://github.com/user-attachments/assets/0be16bfa-6f74-45e9-82f5-837630867367)
