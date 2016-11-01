package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    //region Declarations
    private int costOfCoffee = 5, costOfCream = 1, costOfChocolate = 2;
    /**
     * Number of coffees in the order
     */
    private int quantity = 0;
    /**
     * Constant logcat debug tag
     */
    private static final String DEBUG_TAG = "JDB";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) return true;
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays information about the order and a thank you.
     *
     * @param orderPrice is the cost for the entire order
     * @param cream      tells whether or not the Whipped Cream check box is checked
     * @param chocolate  tells whether or not the Chocolate check box is checked
     * @param name       is the customer's name
     * @return the summary of the order message to display
     */
    private String createOrderSummary(int orderPrice, boolean cream, boolean chocolate, String name) {
        String eol = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(getString(R.string.name)).append(": ").append(name).append(eol);

        if (cream) {
            sb.append(getString(R.string.add_whipped_cream)).append(eol);
        }
        if (chocolate) {
            sb.append(getString(R.string.add_chocolate));
        }
        sb.append(getString(R.string.quantity)).append(": ").append(quantity).append(eol)
                .append(getString(R.string.total)).append(": ").append(NumberFormat.getCurrencyInstance().format(orderPrice))
                .append(eol).append(getString(R.string.thank_you));

        return sb.toString();
    }

    /**
     * This method is called when the Order button is clicked
     *
     * @return the cost of the order
     */
    private int calculateCost() {
        CheckBox creamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_check_box);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_check_box);

        boolean addCream = creamCheckBox.isChecked();
        boolean addChocolate = chocolateCheckBox.isChecked();

        int price = quantity * costOfCoffee;

        if (quantity != 0) {
            if (addCream) {
                price += costOfCream * quantity;
            }
            if (addChocolate) {
                price += costOfChocolate * quantity;
            }
        }

        return price;
    }

    public void submitOrder(View view) {
        CheckBox creamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_check_box);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_check_box);

        boolean addCream = creamCheckBox.isChecked();
        boolean addChocolate = chocolateCheckBox.isChecked();

        EditText nameEdit = (EditText) findViewById(R.id.name_view);
        String name = nameEdit.getText().toString();

        int price = calculateCost();
        composeEmail(createOrderSummary(price, addCream, addChocolate, name));
    }

    /**
     * This method is called when the plus button is clicked
     *
     * @param view is the sender of the onClick event
     */
    public void increment(View view) {
        if (quantity != 10) {

            quantity++;
        } else {
            Toast toastMsg = Toast.makeText(this, getString(R.string.max_coffees_toast), Toast.LENGTH_SHORT);
            toastMsg.show();
        }

        display(quantity);
    }

    /**
     * This method is called when the minus button is clicked
     *
     * @param view is the sender of the onClick event
     */
    public void decrement(View view) {
        if (quantity != 0) {
            quantity--;
        }

        display(quantity);
    }

    /**
     * Sets the text of the quantity of coffees
     *
     * @param number is the quantity of coffee
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(number);
        displayMessage();
        getString(R.string.thank_you);
    }


    public void displayMessage() {
        int price = calculateCost();
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(getString(R.string.total) + NumberFormat.getCurrencyInstance().format(price));
    }

    /**
     * Displays the order summary to the screen
     *
     * @param message is the order summary string
     */
    public void checkboxClicked(View view) {
        displayMessage();
    }

    /**
     * @param text is the String with the body of the email
     */
    public void composeEmail(String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("*/*");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_confirmation));
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
