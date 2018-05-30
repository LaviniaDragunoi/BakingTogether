package com.example.user.bakingtogether.UI.ObjectList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.ViewModel.DetailsActivityViewModel;
import com.example.user.bakingtogether.ViewModel.DetailsViewModelFactory;
import com.example.user.bakingtogether.adapter.ListsAdapter;
import com.example.user.bakingtogether.data.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyListsFragment extends Fragment {
    private static final String LOG_TAG = MyListsFragment.class.getSimpleName();
    @BindView(R.id.object_list_recycler_view)
    RecyclerView objectListRV;
    private Context mContext;
    ListsAdapter objectAdapter;
    private AppRoomDatabase roomDB;
    private int recipeId;
    private DetailsActivityViewModel mViewModel;
    private ArrayList<Parcelable> objectIngredientsList, objectStepsList;
    private TheRepository repository;
    private DetailsViewModelFactory mDetailsViewModelFactory;
  //  OnListClickListener mCallback;

   /* public interface OnListClickListener{
        void onOptionSelected(int recipeId);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (OnListClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }*/
    public MyListsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.object_list_fragment, container, false);
        ButterKnife.bind(this, rootView);

            RecyclerView.LayoutManager layoutManagerReviews = new
                    LinearLayoutManager(mContext);
            objectListRV.setLayoutManager(layoutManagerReviews);

        return rootView;
    }

  public List<Object> convertIngredientListToObjectList(List<IngredientEntity> ingredientEntityList){
      return new ArrayList<>(ingredientEntityList);
    }

    public List<Object> convertStepsListToObjectList(List<StepEntity> stepsEntityList){
        return new ArrayList<>(stepsEntityList);
    }



        public void bindDataToAdapter (List<Object> objectList) {
            // Bind adapter to recycler view object

            objectListRV.setAdapter(new ListsAdapter(mContext, objectList));
        }


    }
