package com.example.unicornmenu.ui.index;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.unicornmenu.R;
import com.example.unicornmenu.ui.index.CircleDisplay.SelectionListener;
import com.example.unicornmenu.databinding.FragmentIndexBinding;

public class IndexFragment extends Fragment implements SelectionListener{

    private FragmentIndexBinding binding;
    private CircleDisplay mCircleDisplay;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding= FragmentIndexBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mCircleDisplay = root.findViewById(R.id.circleDisplay);

        mCircleDisplay.setAnimDuration(4000);
        mCircleDisplay.setValueWidthPercent(55f);
        mCircleDisplay.setFormatDigits(1);
        mCircleDisplay.setDimAlpha(80);
        mCircleDisplay.setSelectionListener(this);
        mCircleDisplay.setTouchEnabled(false);//change
        mCircleDisplay.setUnit("%");
        mCircleDisplay.setStepSize(0.5f);
        mCircleDisplay.showValue(25f, 100f, true);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSelectionUpdate(float val, float maxval) {
        Log.i("Main", "Selection update: " + val + ", max: " + maxval);
    }

    @Override
    public void onValueSelected(float val, float maxval) {
        Log.i("Main", "Selection complete: " + val + ", max: " + maxval);
    }
}
