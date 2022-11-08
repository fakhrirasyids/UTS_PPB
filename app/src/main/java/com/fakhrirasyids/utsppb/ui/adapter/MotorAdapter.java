package com.fakhrirasyids.utsppb.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fakhrirasyids.utsppb.databinding.ItemGridLayoutBinding;
import com.fakhrirasyids.utsppb.databinding.ItemListLayoutBinding;
import com.fakhrirasyids.utsppb.entity.Motor;
import com.fakhrirasyids.utsppb.callback.OnItemClickCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MotorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ListModeViewHolder listModeViewHolder;
    private GridModeViewHolder gridModeViewHolder;
    private OnItemClickCallback onItemClickCallback;

    public ArrayList<Motor> motorList = new ArrayList<>();
    private final int LIST_MODE = 0;
    private final int GRID_MODE = 1;
    Boolean isSwitched = true;

    public MotorAdapter(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ArrayList<Motor> getMotorList() {
        return motorList;
    }

    public void setMotorList(ArrayList<Motor> motorList) {
        if (motorList.size() > 0) {
            this.motorList.clear();
        }
        this.motorList.addAll(motorList);
        notifyDataSetChanged();
    }

    public void addItem(Motor motor) {
        this.motorList.add(motor);
        notifyItemInserted(motorList.size() - 1);
    }

    public void updateItem(int position, Motor motor) {
        this.motorList.set(position, motor);
        notifyItemChanged(position, motor);
    }

    public void removeItem(int position) {
        this.motorList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, motorList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        listModeViewHolder = new ListModeViewHolder(ItemListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        gridModeViewHolder = new GridModeViewHolder(ItemGridLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        if (viewType == LIST_MODE) {
            return listModeViewHolder;
        } else {
            return gridModeViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Motor motor = motorList.get(position);
        if (!isSwitched) {
            listModeViewHolder.setItem(motor);
            listModeViewHolder.itemView.setOnClickListener(view -> onItemClickCallback.onMotorClicked(motor, position));
        } else {
            gridModeViewHolder.setItem(motor);
            gridModeViewHolder.itemView.setOnClickListener(view -> onItemClickCallback.onMotorClicked(motor, position));
        }
    }

    @Override
    public int getItemCount() {
        return motorList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSwitched) {
            return GRID_MODE;
        } else {
            return LIST_MODE;
        }
    }

    public void toggleViewMode(Boolean isList) {
        isSwitched = isList;
    }

    public static class ListModeViewHolder extends RecyclerView.ViewHolder {
        ItemListLayoutBinding binding;

        public ListModeViewHolder(@NonNull ItemListLayoutBinding b) {
            super(b.getRoot());
            binding = b;
        }

        public void setItem(Motor motor) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");

            Bitmap bitmap = BitmapFactory.decodeByteArray(Base64.decode(motor.gambar, Base64.DEFAULT), 0, Base64.decode(motor.gambar, Base64.DEFAULT).length);
            binding.ivMotor.setImageBitmap(bitmap);
            binding.tvMotorName.setText(motor.nama);
            binding.tvKode.setText(motor.kode);
            binding.tvSatuan.setText(new StringBuilder(" / 1 " + motor.satuan));
            binding.tvHarga.setText(new StringBuilder("Rp " + formatter.format(Integer.parseInt(motor.harga))));
            binding.tvStock.setText(new StringBuilder("Stock : " + motor.jumlah));
        }
    }

    public static class GridModeViewHolder extends RecyclerView.ViewHolder {
        ItemGridLayoutBinding binding;

        public GridModeViewHolder(@NonNull ItemGridLayoutBinding b) {
            super(b.getRoot());
            binding = b;
        }

        public void setItem(Motor motor) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");

            Bitmap bitmap = BitmapFactory.decodeByteArray(Base64.decode(motor.gambar, Base64.DEFAULT), 0, Base64.decode(motor.gambar, Base64.DEFAULT).length);
            binding.ivMotor.setImageBitmap(bitmap);
            binding.tvMotorName.setText(motor.nama);
            binding.tvKode.setText(motor.kode);
            binding.tvSatuan.setText(new StringBuilder(" / 1 " + motor.satuan));
            binding.tvHarga.setText(new StringBuilder("Rp " + formatter.format(Integer.parseInt(motor.harga))));
            binding.tvStock.setText(new StringBuilder("Stock : " + motor.jumlah));
        }
    }
}
