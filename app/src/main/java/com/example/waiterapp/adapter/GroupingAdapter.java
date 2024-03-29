package com.example.waiterapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterapp.R;
import com.example.waiterapp.activity.grouping.AddEditGroupingActivity;
import com.example.waiterapp.activity.product.ProductActivity;
import com.example.waiterapp.database.DatabaseHelper;
import com.example.waiterapp.database.dao.GroupingDao;
import com.example.waiterapp.model.Grouping;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupingAdapter extends RecyclerView.Adapter<GroupingAdapter.ViewHolder> implements Filterable {
    Context context;
    DatabaseHelper databaseHelper;
    GroupingDao groupingDao;
    List<Grouping> groupingList , list_search;

    public GroupingAdapter(List<Grouping> groupingList,Context context) {
        this.list_search = groupingList;
        this.groupingList = new ArrayList<>(list_search);
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_grouping,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Grouping grouping = groupingList.get(position);
        holder.grouping_name_tv.setText(grouping.name);


        try {
            if (new File(grouping.picture).exists() && !grouping.picture.isEmpty()){
                Picasso.with(context).load(new File(grouping.picture)).into(holder.grouping_img);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogBSheet(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener , View.OnClickListener{
        ImageView grouping_img;
        TextView grouping_name_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            grouping_img = itemView.findViewById(R.id.grouping_photo_imv);
            grouping_name_tv = itemView.findViewById(R.id.grouping_name_tv);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("groupingToproduct" , new Gson().toJson(groupingList.get(getAdapterPosition())));
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            showDialogBSheet(getAdapterPosition());
            return true;
        }
    }

    private void showDialogBSheet(int pos){

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bttn_sheet_add_edit_grouping);

        LinearLayout edit_grouping_btsh , delete_grouping_btsh ;

        edit_grouping_btsh = bottomSheetDialog.findViewById(R.id.edit_grouping_btsh);
        delete_grouping_btsh = bottomSheetDialog.findViewById(R.id.delete_grouping_btsh);

        Grouping grouping = groupingList.get(pos);

        delete_grouping_btsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("حذف")
                        .setMessage("آیا از حذف این مشتری اطمینان دارید؟")
                        .setPositiveButton("بله", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                databaseHelper = DatabaseHelper.getInstance(context.getApplicationContext());
                                groupingDao = databaseHelper.groupingDao();
                                groupingDao.deleteGrouping(grouping);
                                groupingList.remove(pos);
                                notifyItemRemoved(pos);
                                notifyItemRangeChanged(pos,groupingList.size());
                                bottomSheetDialog.dismiss();
                            }
                        })
                        .setNegativeButton("خیر",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                bottomSheetDialog.dismiss();
                            }
                        })
                        .create()
                        .show();
                bottomSheetDialog.dismiss();
            }
        });

        edit_grouping_btsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AddEditGroupingActivity.class);
                intent.putExtra("Grouping" , new Gson().toJson(groupingList.get(pos)));
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    public void addList(List<Grouping> arrGroupingList){
        list_search.clear();
        list_search.addAll(arrGroupingList);
        groupingList = new ArrayList<>(list_search);
        notifyDataSetChanged();
    }

    //  For search
    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private final Filter newsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Grouping> filterdNewList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filterdNewList.addAll(list_search);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Grouping grouping : list_search){

                    if(grouping.name.toLowerCase().contains(filterPattern))
                        filterdNewList.add(grouping);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdNewList;
            results.count = filterdNewList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            groupingList.clear();
            groupingList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };



}
