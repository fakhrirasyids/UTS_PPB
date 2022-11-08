package com.fakhrirasyids.utsppb.callback;

import com.fakhrirasyids.utsppb.entity.Motor;

import java.util.ArrayList;

public interface LoadMotorCallback {
    void preExecute();

    void postExecute(ArrayList<Motor> motor);
}
