package com.example.waiterapp.activity.product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterapp.R;
import com.example.waiterapp.adapter.GroupingProductAdapter;
import com.example.waiterapp.adapter.ProductAdapter;
import com.example.waiterapp.database.DatabaseHelper;
import com.example.waiterapp.database.dao.GroupingDao;
import com.example.waiterapp.database.dao.ProductDao;
import com.example.waiterapp.helper.App;
import com.example.waiterapp.model.Grouping;
import com.example.waiterapp.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView category_recycler , product_recycler;
    private DatabaseHelper databaseHelper;
    private ProductDao productDao;
    private GroupingDao groupingDao;
    private ProductAdapter productAdapter;
    private GroupingProductAdapter groupingProductAdapter;
    private Toolbar toolbar;
    private TextView noProduct;
    private String category;



    private Boolean for_order = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


//        TextView gg=(TextView) findViewById(R.id)



        databaseHelper= App.getDatabase();
        productDao= databaseHelper.productDao();
        groupingDao = databaseHelper.groupingDao();
        check_intent();

        init();
        set_toolBar();
        set_floatingActtionButton();
        hide_floatingActionButton();





    }

    void check_intent(){
        if(getIntent().getExtras() != null){
            for_order = getIntent().getBooleanExtra("for_order",false);
        }
        if (getIntent().getStringExtra("groupingToproduct") != null) {
            String for_grouping = getIntent().getStringExtra("groupingToproduct");
            Grouping grouping = new Gson().fromJson(for_grouping, Grouping.class);
            category = grouping.name;
        }
    }

    public void init(){
        category_recycler = findViewById(R.id.grouping_product_recycler);
        product_recycler = findViewById(R.id.product_recycler);
        floatingActionButton = findViewById(R.id.product_flabttn);
        noProduct = findViewById(R.id.noProduct);
    }

    public void set_floatingActtionButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, AddEditProductActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);
            }
        });
    }

    public void hide_floatingActionButton(){
        product_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    floatingActionButton.hide();
                }else{
                    floatingActionButton.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void set_grouping_recycler(){



        ArrayList<Grouping> groupingArrayList = new ArrayList<>();
        groupingArrayList.add(0,new Grouping("همه محصولات",""));
        groupingArrayList.addAll(groupingDao.getGroupingList());


        category_recycler.setHasFixedSize(true);
//
//        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL,true);
//        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this ,LinearLayoutManager.HORIZONTAL , false);
        category_recycler.setLayoutManager(layoutManager);

//        category_recycler.addOnScrollListener(new CenterScrollListener());
        groupingProductAdapter = new GroupingProductAdapter(this, groupingArrayList , new GroupingProductAdapter.Listener() {
            @Override
            public void onClick(int pos, Grouping catgry) {
                if(pos == 0){
                    category = null;
                }else {
                    category = catgry.name;
                }
                initListProduct();
            }
        }, category);
        category_recycler.setAdapter(groupingProductAdapter);
    }

    public void set_product_recycler(){

        product_recycler.setHasFixedSize(true);
        productAdapter = new ProductAdapter(new ArrayList<>(), this, new ProductAdapter.Listener() {
            @Override
            public void onClick(Product product, int pos) {
                if(for_order){
                    for_order = getIntent().getBooleanExtra("for_order" , false);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("json_product" , new Gson().toJson(product));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else {
                    productAdapter.showDialogBSheet(pos);
                }
            }
        });
        product_recycler.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        set_grouping_recycler();
        set_product_recycler();
        productAdapter.clear();
        initListProduct();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setBackground(getResources().getDrawable(R.drawable.rippler));

        TextView searchText = (TextView) searchView.findViewById(R.id.search_src_text);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"font/iran_sans.ttf");
        searchText.setTypeface(myCustomFont);



        EditText searchEdit = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        searchEdit.setTextColor(getResources().getColor(R.color.whitediff));
        searchEdit.setHintTextColor(getResources().getColor(R.color.brownlight));
        searchEdit.setTypeface(myCustomFont);
        searchEdit.setHint("جستجوی محصول..");
        searchEdit.setTextSize(14);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newtxt) {
                productAdapter.getFilter().filter(newtxt);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void set_toolBar(){
        toolbar = findViewById(R.id.product_toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(getResources().getColor(R.color.whitediff));
        setSupportActionBar(toolbar);
    }

    private void initListProduct(){
        Log.e("qqqq", "initListProduct: " + category);
        if(productAdapter != null){

            category_recycler.setVisibility(View.VISIBLE);

            if (category == null || category.isEmpty()){
                productAdapter.addList(productDao.getProductList());
            }else {
                productAdapter.addList(productDao.getListByCtegory(category));
            }
        }else {
            category_recycler.setVisibility(View.GONE);
            noProduct.setVisibility(View.VISIBLE);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (databaseHelper != null) databaseHelper.close();
    }
//
//    private void setReverseRecycler() {
//        Tools.setReverseRecycler(this, recyclerView_product);
//    }

}