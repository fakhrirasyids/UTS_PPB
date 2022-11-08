package com.fakhrirasyids.utsppb.callback;

import com.fakhrirasyids.utsppb.entity.Motor;
import com.fakhrirasyids.utsppb.entity.MotorCart;

import java.util.ArrayList;

public interface LoadMotorCartCallback {
    void preExecute();

    void postExecute(ArrayList<MotorCart> motorCart);
}
