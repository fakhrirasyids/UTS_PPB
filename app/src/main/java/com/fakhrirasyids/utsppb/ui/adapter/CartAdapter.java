package com.fakhrirasyids.utsppb.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fakhrirasyids.utsppb.databinding.ItemCartLayoutBinding;
import com.fakhrirasyids.utsppb.databinding.ItemGridLayoutBinding;
import com.fakhrirasyids.utsppb.entity.Motor;
import com.fakhrirasyids.utsppb.entity.MotorCart;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public ArrayList<MotorCart> motorCartList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCartLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void setMotorList(ArrayList<MotorCart> motorCartList) {
        if (motorCartList.size() > 0) {
            this.motorCartList.clear();
        }
        this.motorCartList.addAll(motorCartList);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(motorCartList.get(position));
    }

    @Override
    public int getItemCount() {
        return motorCartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartLayoutBinding binding;

        public ViewHolder(@NonNull ItemCartLayoutBinding b) {
            super(b.getRoot());
            this.binding = b;
        }

        public void setData(MotorCart motorCart) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            int sisa = Integer.parseInt(motorCart.jumlah) - Integer.parseInt(motorCart.terjual);

            Bitmap bitmap = BitmapFactory.decodeByteArray(Base64.decode(motorCart.gambar, Base64.DEFAULT), 0, Base64.decode(motorCart.gambar, Base64.DEFAULT).length);
            binding.ivMotorProfile.setImageBitmap(bitmap);
            binding.tvNama.setText(motorCart.nama);
            binding.tvSatuan.setText(motorCart.satuan);
            binding.tvStock.setText(motorCart.jumlah);
            binding.tvHarga.setText(new StringBuilder("Rp " + formatter.format(Integer.parseInt(motorCart.harga))));
            binding.tvTerjual.setText(motorCart.terjual);
            binding.tvSisa.setText(String.valueOf(sisa));
        }
    }
}
