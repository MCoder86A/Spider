package com.example.spider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
      holder.getUrlText().setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            try {
               String url = url_list.get(holder.getAdapterPosition());
               Uri url_forUri = Uri.parse(url);
               Intent urlVisitIntent = new Intent();
               urlVisitIntent.setAction(Intent.ACTION_VIEW);
               urlVisitIntent.setData(url_forUri);
               view.getContext().startActivity(urlVisitIntent);
            }
            catch (RuntimeException e){
               e.printStackTrace();
               Toast.makeText(view.getContext(), "Invalid url", Toast.LENGTH_SHORT)
                       .show();
            }

         }
      });

      holder.getImageView().setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            FIleManager fm = new FIleManager(view.getContext());
            JSONObject json_root = fm.readFileString2JSON();
            try {
               JSONObject json_urls = json_root.getJSONObject("url");
               json_urls.remove(url_list.get(holder.getAdapterPosition()));
               fm.writeFileJSON2String(json_root);
               removeAt(holder.getAdapterPosition());
            } catch (JSONException e) {
               e.printStackTrace();
            }

         }
      });
   }

   @Override
   public int getItemCount() {
      return url_list.size();
   }

   public void removeAt(int position) {
      url_list.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, url_list.size());
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      private final TextView urlText;
      private final ImageView imageView;

      public ViewHolder(View view) {
         super(view);
         // Define click listener for the ViewHolder's View
         TextView tv = (TextView) view.findViewById(R.id.textView);
         ImageView iv = (ImageView) view.findViewById(R.id.imageView);

         urlText = tv;
         imageView = iv;
      }

      public TextView getUrlText() {
         return urlText;
      }

      public ImageView getImageView(){
         return imageView;
      }

   }


}
