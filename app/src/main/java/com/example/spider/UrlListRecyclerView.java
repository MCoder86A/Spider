package com.example.spider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class UrlListRecyclerView extends RecyclerView.Adapter<UrlListRecyclerView.ViewHolder>{
   ArrayList<String> url_list;
   UrlListRecyclerView(ArrayList<String> url_list){
      this.url_list = url_list;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      View listItem = layoutInflater.inflate(R.layout.url_list, parent, false);
      return new ViewHolder(listItem);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.getUrlText().setText(url_list.get(position));
   }

   @Override
   public int getItemCount() {
      return url_list.size();
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      private final TextView urlText;

      public ViewHolder(View view) {
         super(view);
         // Define click listener for the ViewHolder's View
         TextView tv = (TextView) view.findViewById(R.id.textView);

         urlText = tv;
      }

      public TextView getUrlText() {
         return urlText;
      }
   }


}
