package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private int quantity;
    private final double basePrice = 4.95;
    private double price = basePrice;
    private double toppingOnePrice = 1.05;
    private double toppingTwoPrice = 1.95;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *  This are getter and setter for quantity
     */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     *  This method will calculate Price
     *
     *  @param quantity it will show number of coffees
     */
    private double calculateTotalPrice(int quantity) {
        return price * quantity;
    }

    /**
     *  This method is used for decrementing the value
     */
    public void decrement(View view) {
        setQuantity(getQuantity() - 1);
        if (getQuantity() < 0)
            setQuantity(0);
        displayQuantity(getQuantity());
        displayPrice(getQuantity());
    }

    /**
     *  This method is used for decrementing the value
     */
    public void increment(View view) {
        setQuantity(getQuantity() + 1);
        displayQuantity(getQuantity());
        displayPrice(getQuantity());
    }

    /**
     *  This method is called when Submit button is clicked
     */
    public void orderSubmit(View view) {
        if (quantity > 0) {
            EditText name_text = findViewById(R.id.customer_name);
            Editable name = name_text.getText();
            double price = calculateTotalPrice(quantity);
            String message = createOrderSummary(name, price);
            displayThanks(message);
            //composeEmail("Order For " + name, message);
            //setContentView(R.layout.purchase_done);
            Toast toast = Toast.makeText(getApplicationContext(), R.string.order_placed, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     *  This method will send summary to mail
     *
     *  @param subject   It is subject of mail
     *  @param mail_body It is body for mail
     */
    public void composeEmail(String subject, String mail_body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, mail_body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     *  This method displays the price value on the screen
     *
     * @param quantity it is the quantity of which price will be calculated
     */
    private void displayPrice(int quantity) {
        TextView priceTextView = findViewById(R.id.price_and_order_summary);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(calculateTotalPrice(quantity)));
        priceTextView = findViewById(R.id.price);
        priceTextView.setText(R.string.price);
        priceTextView.setAllCaps(true);

        // to set and clear price on bottom navBar
        if(quantity > 0) {
            priceTextView = findViewById(R.id.bottom_price);
            priceTextView.setText(NumberFormat.getCurrencyInstance().format(calculateTotalPrice(quantity)));
            priceTextView = findViewById(R.id.bottom_total_tag);
            priceTextView.setText(R.string.bottom_price_tag);
        } else {
            priceTextView = findViewById(R.id.bottom_price);
            priceTextView.setText("");
            priceTextView = findViewById(R.id.bottom_total_tag);
            priceTextView.setText("");
        }
    }

    /**
     *  This method displays the quantity value on the screen
     *
     *  @param quantity it will show number of coffees
     */
    private void displayQuantity(int quantity) {
        TextView quantityTextView = findViewById(R.id.quantity_value);
        quantityTextView.setText(NumberFormat.getIntegerInstance().format(quantity));
    }

    /**
     *  This method displays thanks message after user Submits the order
     *
     *  @param msg is is the message to be printed after purchase
     */
    private void displayThanks(String msg) {
        TextView thanksTextView = findViewById(R.id.price);
        thanksTextView.setText(R.string.order_label);
        thanksTextView.setAllCaps(true);
        thanksTextView = findViewById(R.id.price_and_order_summary);
        thanksTextView.setText(msg);
    }

    /**
     *  This method will create final summary
     *
     *  @param nameOfCustomer It is name of customer
     *  @param totalPrice     it is total cost of purchase
     *  @return it returns the final string
     */
    private String createOrderSummary(Editable nameOfCustomer, double totalPrice) {
        String finalMessage = "Name : " + nameOfCustomer;
        finalMessage += "\nQuantity : " + quantity;
        finalMessage += "\nTopping :";
        if (isToppingOne())
            finalMessage += " " + getString(R.string.topping_one);
        if (isToppingTwo())
            finalMessage += " " + getString(R.string.topping_two);
        finalMessage += "\nTotal : " + NumberFormat.getCurrencyInstance().format(totalPrice);
        finalMessage += "\nThank You...";
        return finalMessage;
    }

    /**
     *  This meathod will add topping price to base price
     *
     *  @param priceTopping it is the price of the topping
     */
    private void addToppingPrice(double priceTopping) {
        price += priceTopping;
    }

    /**
     *  This method will subtract price of topping if topping is removed
     *
     *  @param priceTopping it is the price of topping
     */
    private void removeToppingPrice(double priceTopping) {
        price -= priceTopping;
        if(price < basePrice)
            price = basePrice;
    }

    /**
     *  This method will check weather toping is selected or not
     */
    private boolean isToppingOne() {
        CheckBox topping = findViewById(R.id.topping_one);
        return topping.isChecked();
    }

    /**
     *  This method will check weather toping is selected or not
     */
    private boolean isToppingTwo() {
        CheckBox topping = findViewById(R.id.topping_two);
        return topping.isChecked();
    }

    /**
     * This method will add the topping one to the Coffee and add extra cost to total
     *
     * @param view view
     */
    public void addToppingOne(View view) {
            if(isToppingOne())
                addToppingPrice(toppingOnePrice);
            else
                removeToppingPrice(toppingOnePrice);
            displayPrice(quantity);
    }

    /**
     *  This method will add the topping one to the Coffee and add extra cost to total
     *
     *  @param view view
     */
    public void addToppingTwo(View view) {

        if(isToppingTwo())
            addToppingPrice(toppingTwoPrice);
        else
            removeToppingPrice(toppingTwoPrice);
        displayPrice(quantity);
    }


    /**
     *  This method will cancel order and clear all fields
     *
     *  @param view view
     */
    public void cancelOrder(View view) {
        setQuantity(0);
        displayQuantity(quantity);
        displayPrice(quantity);
        CheckBox checkBox = findViewById(R.id.topping_one);
        if(checkBox.isChecked())
            removeToppingPrice(toppingOnePrice);
        checkBox.setChecked(false);
        checkBox = findViewById(R.id.topping_two);
        if(checkBox.isChecked())
            removeToppingPrice(toppingTwoPrice);
        checkBox.setChecked(false);
        EditText editText = findViewById(R.id.customer_name);
        editText.setText("");
        Toast toast = Toast.makeText(getApplicationContext(),R.string.cancel_order, Toast.LENGTH_SHORT);
        toast.show();
    }
}
