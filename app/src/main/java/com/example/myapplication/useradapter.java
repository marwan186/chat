package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class useradapter extends RecyclerView.Adapter<useradapter.userholder> {

    private ArrayList<user> users;
    private Context context;
    private onuserclick onuserclick;

    public useradapter(ArrayList<user> users, Context context, useradapter.onuserclick onuserclick) {
        this.users = users;
        this.context = context;
        this.onuserclick = onuserclick;
    }

    interface onuserclick{
        void onuserclick(int position);
    }
    @NonNull
    @Override
    public userholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_holder, parent, false);
        return new userholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull userholder holder, int position) {
        holder.txtusername.setText(users.get(position).getUsername());
        Glide.with(context).load(users.get(position).getProfilepicture()).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.img_pro);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class userholder extends RecyclerView.ViewHolder{
        TextView txtusername;
        ImageView img_pro;
        public userholder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onuserclick.onuserclick(getAdapterPosition());
                }
            });

            txtusername= itemView.findViewById(R.id.txtusername);
            img_pro= itemView.findViewById(R.id.img_pro);

        }
    }
}
