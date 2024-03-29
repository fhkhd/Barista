package com.example.waiterapp.activity.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.example.waiterapp.adapter.CustomerAdapter;
import com.example.waiterapp.database.DatabaseHelper;
import com.example.waiterapp.database.dao.CustomerDao;
import com.example.waiterapp.helper.App;
import com.example.waiterapp.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private CustomerDao customerDao;
    private Customer customerr;
    private int poss;
    private Toolbar customer_toolbar;
    private CustomerAdapter customerAdapter;
    private boolean for_order = false;
    private SlidrInterface slidrInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        slidrInterface = Slidr.attach(this);

        databaseHelper = App.getDatabase();
        customerDao = databaseHelper.customerDao();

        check_intent();
        init();
        set_floatingActtionButton();
        hide_floatingActionButton();
        set_search();
        set_recyclerView();
    }

    public void check_intent(){
        if (getIntent() != null) {
            for_order = getIntent().getBooleanExtra("for_order", false);
        }
    }

    public void init(){
        recyclerView = findViewById(R.id.customer_recycler);
        floatingActionButton = findViewById(R.id.customer_flabttn);
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_customer,menu);
        MenuItem item = menu.findItem(R.id.menu_item_search_customer);
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
        searchEdit.setHint("جستجوی مشتری..");
        searchEdit.setTextSize(14);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newtxt) {
                customerAdapter.getFilter().filter(newtxt);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void set_search(){

        customer_toolbar = findViewById(R.id.customer_toolbar);
        customer_toolbar.setTitle("");
        customer_toolbar.setTitleTextColor(getResources().getColor(R.color.whitediff));
        setSupportActionBar(customer_toolbar);
    }


    public void set_floatingActtionButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerActivity.this, AddEditCostomerActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);
            }
        });
    }

    public void hide_floatingActionButton(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    public void set_recyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerAdapter = new CustomerAdapter(new ArrayList<>() , this,new CustomerAdapter.Listener(){
            @Override
            public void onClickListener(Customer customer, int pos ,String name) {
//                customerr = customer;
//                poss = pos;
                if (for_order) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("json_customer", new Gson().toJson(customer));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    customerAdapter.showDialogBSheet(pos , name , customer.id);
                }
            }
        },CustomerActivity.this);
        recyclerView.setAdapter(customerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(databaseHelper != null)
//            databaseHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(customerAdapter != null)
            customerAdapter.addList(customerDao.getCustomerList());
    }
}