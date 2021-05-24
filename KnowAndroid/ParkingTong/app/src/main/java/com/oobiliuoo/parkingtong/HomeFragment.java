package com.oobiliuoo.parkingtong;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_TEXT = "param1";

    private static final String TAG = "HomeFragment";

    // TODO: Rename and change types of parameters
    private String mTextString;
    View rootView;

    private CardView cardViewPark,cardViewFindCar,cardViewNotice,cardViewActive,cardViewLastPark,cardViewCarNum;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTextString = getArguments().getString(ARG_TEXT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(rootView == null){
            rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        }

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {

        cardViewNotice = getView().findViewById(R.id.cardView_notice);
        cardViewNotice.setOnClickListener(new lister());

        cardViewActive = getView().findViewById(R.id.cardView_active);
        cardViewActive.setOnClickListener(new lister());


        cardViewPark = getView().findViewById(R.id.cardView_park);
        cardViewPark.setOnClickListener(new lister());


        cardViewLastPark = getView().findViewById(R.id.cardView_parkLast);
        cardViewLastPark.setOnClickListener(new lister());

        cardViewFindCar = getView().findViewById(R.id.cardView_findCar);
        cardViewFindCar.setOnClickListener(new lister());


        cardViewCarNum = getView().findViewById(R.id.cardView_carNum);
        cardViewCarNum.setOnClickListener(new lister());

    }

    class lister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cardView_park:

                    Intent intent = new Intent(getActivity(), HelloBaiduMapActivity.class);
                    startActivity(intent);

                    Log.i(TAG, "onClick: park"  );
                    break;

                case R.id.cardView_findCar:
                    Log.i(TAG, "onClick: fc"  );
                    break;
                case R.id.cardView_parkLast:
                    Log.i(TAG, "onClick: last park"  );
                    break;
                case R.id.cardView_carNum:
                    Log.i(TAG, "onClick: car num"  );
                    break;
                case R.id.cardView_notice:
                    Log.i(TAG, "onClick: notice"  );
                    break;
                case R.id.cardView_active:
                    Log.i(TAG, "onClick: actuve"  );
                    break;


            }

        }
    }




}