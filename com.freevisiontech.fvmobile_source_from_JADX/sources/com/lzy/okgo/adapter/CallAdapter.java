package com.lzy.okgo.adapter;

public interface CallAdapter<T, R> {
    R adapt(Call<T> call, AdapterParam adapterParam);
}
