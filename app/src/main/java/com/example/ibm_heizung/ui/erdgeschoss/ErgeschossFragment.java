package com.example.ibm_heizung.ui.erdgeschoss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ibm_heizung.databinding.FragmentErdgeschossBinding;

public class ErgeschossFragment extends Fragment {

    private FragmentErdgeschossBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ErgeschossViewModel ErgeschossViewModel =
                new ViewModelProvider(this).get(ErgeschossViewModel.class);

        binding = FragmentErdgeschossBinding.inflate(inflater, container, false);

        //    final TextView textView = binding.textSlideshow;
   //     ErgeschossViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}