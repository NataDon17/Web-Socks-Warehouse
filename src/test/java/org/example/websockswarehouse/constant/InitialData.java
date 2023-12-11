package org.example.websockswarehouse.constant;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class InitialData {

    public JSONObject getSockWithNegativeQuantity() {
        JSONObject sockWithNegativeQuantity = new JSONObject();
        sockWithNegativeQuantity.put("color", "pink");
        sockWithNegativeQuantity.put("cottonPart", "70");
        sockWithNegativeQuantity.put("quantity", "-1");
        return sockWithNegativeQuantity;
    }

    public JSONObject getSockWithEmptyColor() {
        JSONObject sockWithEmptyColor = new JSONObject();
        sockWithEmptyColor.put("color", "");
        sockWithEmptyColor.put("cottonPart", "70");
        sockWithEmptyColor.put("quantity", "1");
        return sockWithEmptyColor;
    }

    public JSONObject getSockWithInvalidCottonPart() {
        JSONObject sockWithInvalidCottonPart = new JSONObject();
        sockWithInvalidCottonPart.put("color", "pink");
        sockWithInvalidCottonPart.put("cottonPart", "200");
        sockWithInvalidCottonPart.put("quantity", "1");
        return sockWithInvalidCottonPart;
    }

    public JSONObject getSockWithNegativeCottonPart() {
        JSONObject sockWithNegativeCottonPart = new JSONObject();
        sockWithNegativeCottonPart.put("color", "pink");
        sockWithNegativeCottonPart.put("cottonPart", "-5");
        sockWithNegativeCottonPart.put("quantity", "1");
        return sockWithNegativeCottonPart;
    }
}
