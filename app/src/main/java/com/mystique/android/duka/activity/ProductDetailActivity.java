package com.mystique.android.duka.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mystique.android.duka.Constants;
import com.mystique.android.duka.R;
import com.mystique.android.duka.db.TinyDB;
import com.mystique.android.duka.model.Product;
import com.mystique.android.duka.widget.BadgeDrawable;

import java.util.ArrayList;
import java.util.List;


public class ProductDetailActivity extends AppCompatActivity {

    public static Product selectedProduct;
    private MenuItem itemCart;
    private int cartItems;
    private LayerDrawable icon;
    private FloatingActionButton fabCart;
    private ViewPager vpProdImages;
    private List<String> imagesList = new ArrayList<>();
    private int selectedPosition = 0;
    private productImagesViewPagerAdapter productImagesViewPagerAdapter;
    private TinyDB tinyDB;
    private TextView tvDetails, tvNumRatings, tvCurrentPrice;
    private RatingBar rbProduct;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(getApplicationContext());

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();

        imagesList.add(selectedProduct.getImages().getImage0());
        imagesList.add(selectedProduct.getImages().getImage1());
        imagesList.add(selectedProduct.getImages().getImage2());

        productImagesViewPagerAdapter = new productImagesViewPagerAdapter();
        vpProdImages.setAdapter(productImagesViewPagerAdapter);
        vpProdImages.addOnPageChangeListener(viewPagerPageChangeListener);
        setCurrentItem(selectedPosition);

        tvDetails.setText(selectedProduct.getAbstract());
        tvCurrentPrice.setText("Kes ".concat(String.valueOf(selectedProduct.getValue())));
        tvNumRatings.setText(String.valueOf(selectedProduct.getRating()));
        rbProduct.setRating(selectedProduct.getRating());

        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if isn't in cart then save to cart
                if (!checkifIteminCart(selectedProduct)) {

                    List<Product> favorites = tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class);
                    List<Product> productList = new ArrayList<>(favorites);
                    productList.add(selectedProduct);
                    tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class).clear();
                    tinyDB.putListObject(Constants.CART_PRODUCTS_KEY, productList);
                    //tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class).addAll(productList);

                    /*If a selectedProduct exists in shared preferences then set color to theme_emphasis*/
                    fabCart.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    Snackbar.make(fabCart, getString(R.string.added_cart), Snackbar.LENGTH_SHORT).show();


                    Bundle bundle = new Bundle();
                    bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, Integer.parseInt(selectedProduct.getOriginalId()));
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, selectedProduct.getName());

                    //Logs an app event.
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    //Sets whether analytics collection is enabled for this app on this device.
                    firebaseAnalytics.setAnalyticsCollectionEnabled(true);

                    //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
                    firebaseAnalytics.setMinimumSessionDuration(20000);

                    //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
                    firebaseAnalytics.setSessionTimeoutDuration(500);

                    //Sets the user ID property.
                    firebaseAnalytics.setUserId(String.valueOf(selectedProduct.getOriginalId()));

                    //Sets a user property to a given value.
                    firebaseAnalytics.setUserProperty("Phone", selectedProduct.getName());

                } else {

                    List<Product> favorites = tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class);
                    for (Product product : favorites) {
                        if (product.getOriginalId().equalsIgnoreCase(selectedProduct.getOriginalId())) {
                            favorites.remove(product);
                        }
                    }
                    List<Product> productList = new ArrayList<>(favorites);
                    tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class).clear();
                    tinyDB.putListObject(Constants.CART_PRODUCTS_KEY, productList);
                    //tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class).addAll(productList);


                    /*If a selectedProduct exists in shared preferences then set color to fab_color*/
                    fabCart.setBackgroundColor(getResources().getColor(R.color.fab_color));
                    Snackbar.make(fabCart, getString(R.string.removed_cart), Snackbar.LENGTH_SHORT).show();
                }
                setBadgeCount(ProductDetailActivity.this, icon, String.valueOf(tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class).size()));

            }
        });

    }

    private void initViews() {
        vpProdImages = findViewById(R.id.vpProductImages);
        rbProduct = findViewById(R.id.rbProduct);
        tvNumRatings = findViewById(R.id.tvNumRatings);
        tvCurrentPrice = findViewById(R.id.tvCurrentPrice);
        tvDetails = findViewById(R.id.tvDetails);
        fabCart = findViewById(R.id.fabCart);
    }


    //Popular Products
    private void setCurrentItem(int position) {
        vpProdImages.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        String product = imagesList.get(position);
        //tvTitle.setText(selectedProduct.name);
        //tvPrice.setText(String.valueOf(selectedProduct.value));
    }


    //  adapter
    public class productImagesViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public productImagesViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) ProductDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_viewpager_product, container, false);

            ImageView imageViewPreview = view.findViewById(R.id.image_preview);

            String url = imagesList.get(position);

            Glide.with(ProductDetailActivity.this).load(url)
                    .apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_placeholder))
                    .thumbnail(0.5f)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return imagesList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_cart:
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        itemCart = menu.findItem(R.id.action_cart);
        icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(ProductDetailActivity.this, icon, String.valueOf(tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class).size()));
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /*Checks whether a particular selectedProduct exists in SharedPreferences*/
    public boolean checkifIteminCart(Product checkProduct) {
        boolean check = false;
        List<Product> favorites = tinyDB.getListObject(Constants.CART_PRODUCTS_KEY, Product.class);
        if (favorites != null) {
            for (Product product : favorites) {
                if (product.getOriginalId().equals(checkProduct.getOriginalId())) {
                    check = true;
                    fabCart.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else
                    fabCart.setBackgroundColor(getResources().getColor(R.color.fab_color));
            }
        }
        return check;
    }


    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
