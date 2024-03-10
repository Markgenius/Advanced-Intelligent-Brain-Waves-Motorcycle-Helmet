package com.example.unicornmenu.ui.brainwave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.unicornmenu.databinding.FragmentBrainwaveBinding;


public class BrainwaveFragment extends Fragment {

    private FragmentBrainwaveBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BrainwaveViewModel brainwaveViewModel =
                new ViewModelProvider(this).get(BrainwaveViewModel.class);

        binding = FragmentBrainwaveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
