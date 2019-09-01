package com.mystique.android.duka.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.santalu.emptyview.EmptyView;
import com.mystique.android.duka.Constants;
import com.mystique.android.duka.R;
import com.mystique.android.duka.Util.StkPushUtils;
import com.mystique.android.duka.adapter.CartAdapter;
import com.mystique.android.duka.db.TinyDB;
import com.mystique.android.duka.io.IO;
import com.mystique.android.duka.model.Product;
import com.mystique.android.duka.model.STKPush;
import com.mystique.android.duka.widget.MyDividerItemDecoration;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private List<Product> favorites;
    private CartAdapter adapter;
    public static TextView tvTotal;
    private Button btnBuy;
    private EmptyView empCart;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(getApplicationContext());

        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        empCart = findViewById(R.id.empCart);
        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnBuy = findViewById(R.id.btnBuy);

        favorites = tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class);

        if (favorites == null) {
            empCart.showEmpty();
        } else {
            if (favorites.size() == 0) {
                empCart.showEmpty();
            }
        }

        if (favorites != null) {
            adapter = new CartAdapter(CartActivity.this, favorites, tvTotal);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rvCart.setLayoutManager(mLayoutManager);
            rvCart.setItemAnimator(new DefaultItemAnimator());
            rvCart.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
            rvCart.setAdapter(adapter);

        }

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialDialog materialDialog = new MaterialDialog.Builder(CartActivity.this)
                        .title(R.string.confirm_purchase)
                        .inputType(InputType.TYPE_CLASS_PHONE)
                        .inputRange(10, 13)
                        .input(getString(R.string.phone_number), null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                if (!StkPushUtils.isValidMobile(input.toString().trim())) {
                                    dialog.getInputEditText().setError(getString(R.string.err_phone));
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                } else {
                                    dialog.getInputEditText().setError(null);
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                }
                            }
                        })
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                                String amount = tvTotal.getText().toString().replace("Ksh ", "").trim();

                                String timestamp = StkPushUtils.getTimestamp();
                                STKPush stkPush = new STKPush(
                                        Constants.BUSINESS_SHORT_CODE,
                                        StkPushUtils.getPassword(Constants.BUSINESS_SHORT_CODE, Constants.PASSKEY, timestamp),
                                        timestamp,
                                        Constants.TRANSACTION_TYPE,
                                        amount,
                                        StkPushUtils.sanitizePhoneNumber(StkPushUtils.sanitizePhoneNumber(dialog.getInputEditText().getText().toString().trim())),
                                        Constants.PARTYB,
                                        StkPushUtils.sanitizePhoneNumber(StkPushUtils.sanitizePhoneNumber(dialog.getInputEditText().getText().toString().trim())),
                                        Constants.CALLBACK_URL,
                                        "test", //The account reference
                                        "test"  //The transaction description
                                );

                                new IO().getAuthToken(CartActivity.this, stkPush);

                            }
                        }).build();
                materialDialog.show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_delete:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
