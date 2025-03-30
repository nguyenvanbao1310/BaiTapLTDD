package com.example.fragment_tablayout_viewpager2.adapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fragment_tablayout_viewpager2.fragment.CancelFragment;
import com.example.fragment_tablayout_viewpager2.fragment.DeliveryFragment;
import com.example.fragment_tablayout_viewpager2.fragment.EvaluateFragment;
import com.example.fragment_tablayout_viewpager2.fragment.NewOrderFragment;
import com.example.fragment_tablayout_viewpager2.fragment.PickupFragment;

public class ViewPaper2Adapter extends FragmentStateAdapter {
    public ViewPaper2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new NewOrderFragment();
            case 1:
                return new PickupFragment();
            case 2:
                return new DeliveryFragment();
            case 3:
                return new EvaluateFragment();
            case 4:
                return new CancelFragment();
            default:
                return new NewOrderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}