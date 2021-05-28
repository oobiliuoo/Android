package com.oobiliuoo.parkingtong.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.net.TcpClient;
import com.oobiliuoo.parkingtong.utils.Utils;

import static android.app.Activity.RESULT_OK;

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

    private static final String IP_ADDRESS = "192.168.43.239";
    private static final int IP_PORT = 8777;

    private ImageButton imageButton;
    private TextView uName;

    private Handler mHandler;
    private TcpClient mTcpClient;

    private boolean isReceiveRun = true;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        imageButton = getView().findViewById(R.id.mine_btn_login);
        imageButton.setOnClickListener(new Lister());

        uName = getView().findViewById(R.id.mine_tv_uName);

        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        // 连接成功
                        Utils.showToast(getContext(),"连接成功");
                        break;
                    case 2:
                        // 连接失败
                        Utils.showToast(getContext(),"连接失败");
                        break;
                    case 3:
                        // 接收成功
                        Utils.showToast(getContext(),"接收成功"+msg.obj);
                        uName.setText(msg.obj.toString());
                        break;
                    default:
                        break;
                }
            }
        };

    }

    class Lister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mine_btn_login:
                    Utils.showToast(getContext(),"登录");
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),1);
                   /*
                    mTcpClient = new TcpClient(mHandler,IP_ADDRESS,IP_PORT);
                    mTcpClient.setSendMsg("LOGIN::admin::123456");
                    mTcpClient.send();
                    */
                    break;

                default:
                    break;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String returnedData = data.getStringExtra("login_return");
                    Utils.showToast(getContext(),returnedData);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}