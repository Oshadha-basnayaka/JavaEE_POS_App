package lk.ijse.jsp.pos.servlet;

import lk.ijse.jsp.pos.servlet.util.DBConnection;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

        if (discount.equals("NaN")) {
            discount = "0";
        }

        String customerID = customer.getString("id");

        System.out.println(cart);

        String itemCode = "";

        int qty = 0;

        for (JsonValue cartItemValue : cart) {
            JsonObject cartItem = (JsonObject) cartItemValue;
            JsonObject item = cartItem.getJsonObject("item");

            itemCode = item.getString("code");
            qty = item.getInt("qty");

            System.out.println(itemCode);
            System.out.println(qty);
        }

        //transaction


        try {
            Connection connection = DBConnection.getDBConnection().getConnection();
            connection.setAutoCommit(false);

            PreparedStatement pstm2 = connection.prepareStatement("insert into orders (orderID, date, customerID, discount, total)\n" +
                    "values (?,?,?,?,?);");


            pstm2.setObject(1, orderID);
            pstm2.setObject(2, date);
            pstm2.setObject(3, customerID);
            pstm2.setObject(4, discount);
            pstm2.setObject(5, total);

            if (pstm2.executeUpdate() > 0) {
                for (JsonValue cartItemValue : cart) {
                    JsonObject cartItem = (JsonObject) cartItemValue;
                    JsonObject item = cartItem.getJsonObject("item");


                    itemCode = item.getString("code");
                    qty = item.getInt("qty");


                    PreparedStatement pstm = connection.prepareStatement("insert into order_items (orderID, itemID, qty)\n" +
                            "values (?,?,?);");

                    pstm.setObject(1, orderID);
                    pstm.setObject(1, itemCode);
                    pstm.setObject(1, String.valueOf(qty));

                    pstm.execute();

                    connection.commit();
                    showMessage(resp, orderID + " Order Successfully Added..!", "ok", "[]");
                    resp.setStatus(200);
                }


            } else {

                connection.rollback();
                showMessage(resp, "Wrong data", "error", "[]");
                resp.setStatus(400);
            }
            connection.setAutoCommit(true);


        } catch (ClassNotFoundException e) {
            showMessage(resp, e.getMessage(), "error", "[]");
            resp.setStatus(500);

        } catch (SQLException e) {
            showMessage(resp, e.getMessage(), "error", "[]");
            resp.setStatus(400);
        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Headers", "Content-type");
    }


    private void showMessage(HttpServletResponse resp, String message, String state, String data) throws IOException {
        JsonObjectBuilder response = Json.createObjectBuilder();
        response.add("state", state);
        response.add("message", message);
        response.add("data", data);
        resp.getWriter().print(response.build());
    }
}
