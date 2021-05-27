package com.oobiliuoo.parkingtong.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.net.TcpClient;
import com.oobiliuoo.parkingtong.net.TcpReceiveThread;
import com.oobiliuoo.parkingtong.net.TcpSendThread;
import com.oobiliuoo.parkingtong.utils.Utils;

import java.net.InetAddress;
import java.net.Socket;

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

    private ImageButton imageButton;

    private Socket socket = null;

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
        imageButton.setOnClickListener(new lister());
    }

    class lister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mine_btn_login:
                    Utils.showToast(getContext(),"登录");

                    //启动连接线程
                    Connect_Thread connect_Thread = new Connect_Thread();
                    connect_Thread.start();

                    /*
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            TcpClient mTcpClient = new TcpClient("192.168.43.1",8181);
                            String message="我是客户端a";
                            mTcpClient.connect();
                            mTcpClient.writeMsg("123");

                        }
                    }).start();

                     */

                    break;

            }

        }
    }


    class Connect_Thread extends Thread//继承Thread
    {
        public void run()//重写run方法
        {
            try
            {
                if (socket == null) //如果已经连接上了，就不再执行连接程序
                {
//用InetAddress方法获取ip地址
                    InetAddress ipAddress = InetAddress.getByName("192.168.43.1");
                    int port = 8181;//获取端口号
                    socket = new Socket(ipAddress, port);//创建连接地址和端口-------------------这样就好多了
                }

            }
            catch (Exception e)
            {
            // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}