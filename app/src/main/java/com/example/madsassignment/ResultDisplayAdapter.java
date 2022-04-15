package com.example.madsassignment;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultDisplayAdapter extends RecyclerView.Adapter<ResultDisplayAdapter.MyRoomViewHolder> {

    private Context context;
    private ArrayList<Result> resultList;
    int selectPosition = -1;
    private String TAG = this.getClass().getSimpleName();



    public ResultDisplayAdapter(Context context, ArrayList<Result> list) {
        this.context = context;
        this.resultList = list;
        selectPosition = 0;
    }
    public class MyRoomViewHolder extends RecyclerView.ViewHolder {
        TextView txt_expression,txt_result;


        public MyRoomViewHolder(@NonNull View view) {
            super(view);


            txt_result = itemView.findViewById(R.id.txt_result);
            txt_expression = itemView.findViewById(R.id.txt_expression);
        }
    }
    @NonNull
    @Override
    public MyRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_addition_view, parent, false);
        return new MyRoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRoomViewHolder holder, int position) {

        try {
            Result result = resultList.get(position);
            holder.txt_expression.setText(result.getExpression());
            holder.txt_result.setText(result.getResult());
        }
        catch (Exception ex)
        {
            Log.e(TAG,"----------------------"+ex.getMessage());
        }



    }


    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void reload(ArrayList<Result> List) {
        resultList = List;

    }


}
