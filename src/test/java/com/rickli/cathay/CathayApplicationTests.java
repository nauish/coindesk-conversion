package com.rickli.cathay;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CathayApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    // 測試呼叫查詢幣別對應表資料 API,並顯示其內容。
    @Test
    void testGetCurrencyApi() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/currency").accept(MediaType.APPLICATION_JSON ))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    // 測試呼叫新增幣別對應表資料 API。
    @Test
    void testPostCurrencyApi() throws Exception {
        JSONObject currencyObj = new JSONObject();
        currencyObj.put("code", "JPY");
        currencyObj.put("name","日圓");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/currency")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(String.valueOf(currencyObj)))
                                    .andExpect(status().isCreated())
                                    .andReturn();

        String responseContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals(currencyObj.toString(), responseContent,false);
    }

    // 測試呼叫更新幣別對應表資料 API,並顯示其內容。
    @Test
    void testPutCurrencyApi() throws Exception {
        JSONObject updatedCurrencyObj = new JSONObject();
        updatedCurrencyObj.put("code", "USD");
        updatedCurrencyObj.put("name", "美元");

        // 測試更新不存在的貨幣代碼
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/currency/{code}", "INVALID")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(updatedCurrencyObj)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Currency with code INVALID not found."));

        // 測試空網址
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/currency/{code}", "  ")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(updatedCurrencyObj)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid updated currency data."));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/currency/{code}", "USD")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(updatedCurrencyObj)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("美元"))
                .andDo(MockMvcResultHandlers.print());
    }

    // 測試呼叫刪除幣別對應表資料 API。
    @Test
    void testDeleteCurrencyApi() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/currency/{code}", "USD")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 測試更新不存在的貨幣代碼
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/currency/{code}", "INVALID")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Currency with code INVALID not found."))
                .andDo(MockMvcResultHandlers.print());
    }
    // 測試呼叫 Coindesk API,並顯示其內容。
    @Test
    void testCoindeskApi() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/coindesk").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time.updated").exists())
                .andExpect(jsonPath("$.time.updatedISO").exists())
                .andExpect(jsonPath("$.time.updateduk").exists())
                .andExpect(jsonPath("$.disclaimer").exists())
                .andExpect(jsonPath("$.chartName").exists())
                .andExpect(jsonPath("$.bpi").exists())
                .andExpect(jsonPath("$.bpi").isMap())
                .andExpect(jsonPath("$.bpi.*").isNotEmpty())
                .andExpect(jsonPath("$.bpi.*.code").exists())
                .andExpect(jsonPath("$.bpi.*.symbol").exists())
                .andExpect(jsonPath("$.bpi.*.rate").exists())
                .andExpect(jsonPath("$.bpi.*.description").exists())
                .andExpect(jsonPath("$.bpi.*.rate_float").exists())
                .andDo(MockMvcResultHandlers.print());
    }
    // 測試呼叫資料轉換的 API,並顯示其內容。
    @Test
    void testTransformedApi() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/coindesk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bpi.USD.name").exists())
                .andExpect(jsonPath("$.bpi.GBP.name").exists())
                .andExpect(jsonPath("$.bpi.EUR.name").exists())
                .andDo(MockMvcResultHandlers.print());

    }
}
