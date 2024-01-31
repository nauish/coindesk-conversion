# Currency Maintenance and Coindesk Conversion Server

## 專案
+ Java JDK: OpenJDK 8
+ Spring Boot: 2.7.6
+ 資料庫:H2(OpenJPA / Spring Data JPA)

## 建立 SQL 語法
+ 建立資料表語法位於 `/src/resources/schema.sql`
```
CREATE TABLE "CURRENCY" (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL,
    name VARCHAR(10) NOT NULL
);
```

+ 插入起始資料語法位於 `/src/resources/data.sql`
```
INSERT INTO "CURRENCY" (code, name) VALUES
                                        ('USD', '美金'),
                                        ('EUR', '歐元'),
                                        ('GBP', '英鎊');
```

## 功能簡述
1. 呼叫 coindesk API，解析其下行內容與資料轉換，並實作新的 API。
   Coindesk API:https://api.coindesk.com/v1/bpi/currentprice.json
2. 建立一張幣別與其對應中文名稱的資料表(需附建立 SQL 語法),並提
   供 查詢 / 新增 / 修改 / 刪除 功能 API。

## 實作內容:
`以下預設都會起在 http://localhost:8080`
1. 幣別 DB 維護功能。
   + `GET`查詢所有貨幣代碼及中文名`/api/v1/currency/`
     ```
     // Response: 
     
        [
           {
           "id": 1,
           "code": "USD",
           "name": "美金"
           },
           {
           "id": 2,
           "code": "EUR",
           "name": "歐元"
           },
           {
           "id": 3,
           "code": "GBP",
           "name": "英鎊"
           }
       ]
     ```
   + `POST`新增代碼與中文名`/api/v1/currency/`
       ```
        // Request: 
     
       {
        "code": "TWD",
        "name": "新台幣"
        }
       ```
     ```
     // Response:
     
     {
       "id": 4,
       "code": "TWD",
       "name": "新台幣"
       }
     ```
   + `PUT`修改代碼代號或中文名 `/api/v1/currency/{代碼}`
       ```
      // Request to /api/v1/currency/TWD:
     
        {    
        "code": "TWD",
        "name": "台幣"
        }
       ```
        ```
     // Response: 
     
     {
       "id": 4,
       "code": "TWD",
       "name": "台幣"
       }
     ```
     
    + `DELETE`刪除貨幣 `/api/v1/currency/{代碼}`
       ```
      // REQUEST to /api/v1/currency/EUR
       ```
      
        ```
      // Response
      {
       "message": "Deleted"
       }
      ```
2. 呼叫 coindesk 的 API。 
    ```GET /api/v1/coindesk```
    ```
   {
    "time": {
        "updated": "Jan 31, 2024 04:29:12 UTC",
        "updatedISO": "2024-01-31T04:29:12+00:00",
        "updateduk": "Jan 31, 2024 at 04:29 GMT"
    },
    "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
    "chartName": "Bitcoin",
    "bpi": {
        "USD": {
            "code": "USD",
            "symbol": "&#36;",
            "rate": "42,947.158",
            "description": "United States Dollar",
            "rate_float": 42947.1583
        },
        "GBP": {
            "code": "GBP",
            "symbol": "&pound;",
            "rate": "33,878.995",
            "description": "British Pound Sterling",
            "rate_float": 33878.9946
        },
        "EUR": {
            "code": "EUR",
            "symbol": "&euro;",
            "rate": "39,697.347",
            "description": "Euro",
            "rate_float": 39697.3468
        }
    }
    }
   ```

3. 呼叫 coindesk 的 API,並進行資料轉換,組成新 API。
   此新 API 提供:
   + 更新時間(時間格式範例:1990/01/01 00:00:00)。 
   + 幣別相關資訊(幣別,幣別中文名稱,以及匯率)。
   ```GET /api/v2/coindesk```
     ```
        {
         "time": {
             "updated": "2024/01/31 04:30:40",
             "updatedISO": "2024-01-31T04:30:40+00:00",
             "updateduk": "Jan 31, 2024 at 04:30 GMT"
         },
         "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
         "chartName": "Bitcoin",
         "bpi": {
             "USD": {
                 "code": "USD",
                 "symbol": "&#36;",
                 "rate": "42,944.545",
                 "description": "United States Dollar",
                 "rate_float": 42944.5446,
                 "name": "美金"
             },
             "GBP": {
                 "code": "GBP",
                 "symbol": "&pound;",
                 "rate": "33,876.933",
                 "description": "British Pound Sterling",
                 "rate_float": 33876.9328,
                 "name": "英鎊"
             },
             "EUR": {
                 "code": "EUR",
                 "symbol": "&euro;",
                 "rate": "39,694.931",
                 "description": "Euro",
                 "rate_float": 39694.9309,
                 "name": "歐元"
             }
         }
     }
      ```

## 單元測試:
1. 測試呼叫查詢幣別對應表資料 API,並顯示其內容。 
```testGetCurrencyApi()```
2. 測試呼叫新增幣別對應表資料 API。
```testPostCurrencyApi()```
3. 測試呼叫更新幣別對應表資料 API,並顯示其內容。 
```testPutCurrencyApi()```
4. 測試呼叫刪除幣別對應表資料 API。 
```testDeleteCurrencyApi()```
5. 測試呼叫 coindesk API,並顯示其內容。 
```testCoindeskApi()```
6. 測試呼叫資料轉換的 API,並顯示其內容。
```testTransformedApi()```