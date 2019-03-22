package com.example.attendencemanager.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.attendencemanager.R;
import com.example.attendencemanager.activities.model.Model;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
     private ArrayList<Model> arrayList;
     private OnRawClickListener mOnRawListener;
     private OnButtonClickListener mOnButtonListener;
     private OnLongHoldListener mOnLongHoldListener;

     public MyAdapter(ArrayList<Model> arrayList, OnRawClickListener onRawClickListener, OnButtonClickListener onButtonClickListener, OnLongHoldListener onLongHoldListener) {
          this.arrayList = arrayList;
          this.mOnRawListener = onRawClickListener;
          this.mOnButtonListener = onButtonClickListener;
          this.mOnLongHoldListener = onLongHoldListener;
     }

     @Override
     public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
          LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
          View view = layoutInflater.inflate(R.layout.raw, viewGroup, false);
          return new MyHolder(view, mOnRawListener, mOnButtonListener, mOnLongHoldListener);
     }

     @Override
     public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {
          Model model = arrayList.get(position);
          String p,a,s;
          //data setting from model to textview
          p=model.getPresent();
          a=model.getAbsent();

          float f = (Float.parseFloat(p)/(Float.parseFloat(p) + Float.parseFloat(a)))*100;
          s= String.format("%.2f",f)+"%";

          if(s.equals("NaN%"))
               s="";
          s= model.getTitle() + "  " +s;
          myHolder.textViewTitle.setText(s);
          p = "P:  " + p;
          myHolder.textViewPresent.setText(p);
          a = "A:  " + a;
          myHolder.textViewAbsent.setText(a);


     }

     @Override
     public int getItemCount() {
          return arrayList.size();
     }

     class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

          TextView textViewTitle, textViewPresent, textViewAbsent, edit, delete;
          Button pButton, aButton;
          OnRawClickListener onRawClickListener;
          OnButtonClickListener onButtonClickListener;
          OnLongHoldListener onLongHoldListener;


          MyHolder(View itemView, OnRawClickListener onRawClickListener, OnButtonClickListener onButtonClickListener, OnLongHoldListener onLongHoldListener) {
               super(itemView);
               textViewTitle = itemView.findViewById(R.id.textView_Subject);
               textViewPresent = itemView.findViewById(R.id.textView_present);
               textViewAbsent = itemView.findViewById(R.id.textView_absent);
               pButton = itemView.findViewById(R.id.p);
               aButton = itemView.findViewById(R.id.a);

               edit = itemView.findViewById(R.id.text1);
               delete = itemView.findViewById(R.id.text2);

               this.onRawClickListener = onRawClickListener;
               itemView.setOnClickListener(this);

               this.onButtonClickListener = onButtonClickListener;
               pButton.setOnClickListener(this);
               aButton.setOnClickListener(this);

               this.onLongHoldListener = onLongHoldListener;
               itemView.setOnLongClickListener(this);
               itemView.setOnLongClickListener(this);
          }


          @Override
          public void onClick(View v) {
               if (v.getId() == pButton.getId())
                    onButtonClickListener.onButtonClick(getAdapterPosition(),1);
               else if(v.getId() == aButton.getId())
                    onButtonClickListener.onButtonClick(getAdapterPosition(),0);
               else
                    onRawClickListener.onRawClick(getAdapterPosition());
          }

          @Override
          public boolean onLongClick(View v) {
               onLongHoldListener.onLongHold(getAdapterPosition());
               return false;
          }
     }

     public interface OnRawClickListener {
          void onRawClick(int position);
     }

     public interface OnButtonClickListener {
          void onButtonClick(int position, int PorA);
     }

     public interface OnLongHoldListener {
          void onLongHold(int position);
     }
}
