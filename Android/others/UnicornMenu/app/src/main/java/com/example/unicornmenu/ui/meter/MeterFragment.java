package com.example.unicornmenu.ui.meter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.unicornmenu.databinding.FragmentMeterBinding;

public class MeterFragment extends Fragment {

    private FragmentMeterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MeterViewModel meterViewModel =
                new ViewModelProvider(this).get(MeterViewModel.class);

        binding = FragmentMeterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

