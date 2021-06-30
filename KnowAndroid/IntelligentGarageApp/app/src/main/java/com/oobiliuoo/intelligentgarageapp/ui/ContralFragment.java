package com.oobiliuoo.intelligentgarageapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oobiliuoo.intelligentgarageapp.R;
import com.oobiliuoo.intelligentgarageapp.net.TcpClient;
import com.oobiliuoo.intelligentgarageapp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContralFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;

    private Handler mHandler;
    private TcpClient mTcpClient;

    public ContralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContralFragment newInstance(String param1, String param2) {
        ContralFragment fragment = new ContralFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        //
                        mTcpClient.receiveMsg();
                        break;
                    case 2:
                        // 连接失败
                        Utils.showToast(getContext(), "连接失败");
                        break;
                    case 3:
                        // 接收成功
                       // changeUI(infoList);

                        Utils.showToast(getContext(), msg.obj.toString());
                        break;
                    case  4:
                        //initCarInfo();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_contral, container, false);
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
      //  initNet();

    }

    private void initNet() {

        // Android 4.0 之后不能在主线程中请求HTTP请求
        new Thread(new Runnable(){
            @Override
            public void run() {
                mTcpClient = new TcpClient(mHandler, Utils.IP_ADDRESS,Utils.IP_PORT);
                mTcpClient.connect();
            }
        }).start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTcpClient.closeConnection();
    }
}