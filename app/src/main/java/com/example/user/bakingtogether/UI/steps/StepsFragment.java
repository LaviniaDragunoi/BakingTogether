package com.example.user.bakingtogether.UI.steps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingtogether.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepsFragment extends Fragment {

        @BindView(R.id.steps_recycler_view)
        RecyclerView steps_recycler_view;
        private Unbinder unbinder;
        public StepsFragment(){

        }
        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
            View rootView = inflater.inflate(R.layout.steps_list_fragment, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            return rootView;
        }
}
