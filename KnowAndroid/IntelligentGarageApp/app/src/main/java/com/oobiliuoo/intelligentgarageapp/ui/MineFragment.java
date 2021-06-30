package com.oobiliuoo.intelligentgarageapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.oobiliuoo.intelligentgarageapp.R;
import com.oobiliuoo.intelligentgarageapp.net.TcpClient;
import com.oobiliuoo.intelligentgarageapp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private EditText etIp;
    private EditText etPort;
    private EditText etSend;
    private Button btnConnect;
    private Button btnSend;
    private Handler mHandler;

    TcpClient tcpClient;


    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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
                        tcpClient.receiveMsg();
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
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        etIp = getActivity().findViewById(R.id.mine_et_ip);
        etPort = getActivity().findViewById(R.id.mine_et_port);
        etSend = getActivity().findViewById(R.id.mine_et_send);

        btnConnect = getActivity().findViewById(R.id.mine_btn_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNet();
            }
        });
        btnSend = getActivity().findViewById(R.id.minr_btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcpClient.sendMsg(etSend.getText().toString());
            }
        });


    }

    private void initNet() {

        // Android 4.0 之后不能在主线程中请求HTTP请求
        new Thread(new Runnable(){
            @Override
            public void run() {

                int port =Integer.valueOf(etPort.getText().toString()).intValue();
                tcpClient = new TcpClient(mHandler, etIp.getText().toString(),port);
                tcpClient.connect();
            }
        }).start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tcpClient.closeConnection();
    }
}