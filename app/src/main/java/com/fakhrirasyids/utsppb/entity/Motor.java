package com.fakhrirasyids.utsppb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Motor extends ArrayList<Parcelable> implements Parcelable {
    public int id;
    public String kode;
    public String nama;
    public String satuan;
    public String harga;
    public String jumlah;
    public String gambar;

    @NonNull
    @Override
    public Stream<Parcelable> stream() {
        return super.stream();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.kode);
        dest.writeString(this.nama);
        dest.writeString(this.satuan);
        dest.writeString(this.harga);
        dest.writeString(this.jumlah);
        dest.writeString(this.gambar);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.kode = source.readString();
        this.nama = source.readString();
        this.satuan = source.readString();
        this.harga = source.readString();
        this.jumlah = source.readString();
        this.gambar = source.readString();
    }

    public Motor() {
    }

    protected Motor(Parcel in) {
        this.id = in.readInt();
        this.kode = in.readString();
        this.nama = in.readString();
        this.satuan = in.readString();
        this.harga = in.readString();
        this.jumlah = in.readString();
        this.gambar = in.readString();
    }

    public static final Parcelable.Creator<Motor> CREATOR = new Parcelable.Creator<Motor>() {
        @Override
        public Motor createFromParcel(Parcel source) {
            return new Motor(source);
        }

        @Override
        public Motor[] newArray(int size) {
            return new Motor[size];
        }
    };
}
