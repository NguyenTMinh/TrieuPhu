package com.example.trieuphu.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.trieuphu.R;
import com.example.trieuphu.intf.OnActionClick;

public abstract class BaseFragment<V extends ViewModel,T extends ViewDataBinding> extends Fragment {
    protected V viewModel;
    protected T dataBinding;
    protected Context context;
    protected OnActionClick actionClick;
    protected MediaPlayer mediaPlayer;

    public void setActionClick(OnActionClick actionClick) {
        this.actionClick = actionClick;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater,getLayout(),container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(getClassVM());
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    protected abstract int getLayout();
    protected abstract Class<V> getClassVM();
    protected abstract void init();
}
