package com.example.trieuphu.view.fragment;


import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.trieuphu.R;
import com.example.trieuphu.adapter.RankAdapter;
import com.example.trieuphu.databinding.FragmentHighscoreBinding;
import com.example.trieuphu.model.Player;
import com.example.trieuphu.viewmodel.HomeViewModel;

import java.util.List;

public class HighScoreFragment extends BaseFragment<HomeViewModel, FragmentHighscoreBinding> {
    private RankAdapter rankAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_highscore;
    }

    @Override
    protected Class<HomeViewModel> getClassVM() {
        return HomeViewModel.class;
    }

    @Override
    protected void init() {
        if (viewModel.getRankList().getValue() != null){
            rankAdapter = new RankAdapter(viewModel.getRankList().getValue(),context);
            dataBinding.rvHighScore.setAdapter(rankAdapter);
            dataBinding.rvHighScore.setLayoutManager(new LinearLayoutManager(context));
        }
    }
}
