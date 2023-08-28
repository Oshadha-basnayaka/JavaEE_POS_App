package lk.ijse.jsp.pos.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/pages/purchase-order"})
public class PurchaseOrderServletAPI extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.addHeader("Content-Type", "application/json");
                resp.addHeader("Access-Control-Allow-Origin", "*");


        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();


        String orderID = jsonObject.getString("orderID");
        String date = jsonObject.getString("date");
        JsonObject customer = jsonObject.getJsonObject("customer");
        JsonArray cart = jsonObject.getJsonArray("cart");
        String total = jsonObject.getString("total");
        String discount = jsonObject.getString("discount");

        if (discount.equals("NaN")){
            discount="0";
        }

        String customerID = customer.getString("id");

        System.out.println(cart);

        String itemCode = "";

        int qty = 0;

        for (JsonValue cartItemValue : cart){
            JsonObject cartItem = (JsonObject) cartItemValue;
            JsonObject item = cartItem.getJsonObject("item");

            itemCode =item.getString("code");
                    qty=item.getInt("qty");

            System.out.println(itemCode);
            System.out.println(qty);
        }

        //transaction


        try{

        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }


    private void showMessage(HttpServletResponse resp, String message, String state, String data) throws IOException {
        JsonObjectBuilder response = Json.createObjectBuilder();
        response.add("state", state);
        response.add("message", message);
        response.add("data", data);
        resp.getWriter().print(response.build());
    }
}
