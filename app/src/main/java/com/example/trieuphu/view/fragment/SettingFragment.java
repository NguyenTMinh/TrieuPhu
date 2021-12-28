package com.example.trieuphu.view.fragment;

import android.widget.CompoundButton;

import androidx.lifecycle.Observer;

import com.example.trieuphu.R;
import com.example.trieuphu.databinding.FragmentSettingBinding;
import com.example.trieuphu.viewmodel.HomeViewModel;

public class SettingFragment extends BaseFragment<HomeViewModel, FragmentSettingBinding>{
    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected Class<HomeViewModel> getClassVM() {
        return HomeViewModel.class;
    }

    @Override
    protected void init() {
        dataBinding.swBgMusic.setChecked(viewModel.getSwitchMusicBGState().getValue());
        dataBinding.swBgMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setSwitchMusicBGState(isChecked);
            }
        });
    }
}
