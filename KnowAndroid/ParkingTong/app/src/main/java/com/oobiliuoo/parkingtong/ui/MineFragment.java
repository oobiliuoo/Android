package com.oobiliuoo.parkingtong.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.database.UsersInfo;
import com.oobiliuoo.parkingtong.utils.Utils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

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
    private TextView uName;
    private TextView tvNickName;
    private TextView tvGender;
    private TextView tvTel;
    private Button btnQuit;
    private TextView tvCarNum;
    private ImageButton ibAddCar;
    private ConstraintLayout changeInfo;

    private Handler mHandler;
    private List<String> infoList;

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

        infoList = new ArrayList<>();
        imageButton = getView().findViewById(R.id.mine_btn_login);
        imageButton.setOnClickListener(new Lister());
        btnQuit = getView().findViewById(R.id.mine_btn_quit);
        btnQuit.setOnClickListener(new Lister());

        uName = getView().findViewById(R.id.mine_tv_name);
        tvNickName = getView().findViewById(R.id.mine_tv_nickName);
        tvGender = getView().findViewById(R.id.mine_tv_gender);
        tvTel = getView().findViewById(R.id.mine_tv_tel);

        tvCarNum = getView().findViewById(R.id.mine_tv_carNum);
        ibAddCar = getView().findViewById(R.id.mine_btn_addCar);
        ibAddCar.setOnClickListener(new Lister());

        changeInfo = getView().findViewById(R.id.mine_btn_changeInfo);
        changeInfo.setOnClickListener(new Lister());

        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        // 连接成功
                        Utils.showToast(getContext(), "连接成功");
                        break;
                    case 2:
                        // 连接失败
                        Utils.showToast(getContext(), "连接失败");
                        break;
                    case 3:
                        // 接收成功
                        changeUI(infoList);
                        break;
                    default:
                        break;
                }
            }
        };
        // 读取当前登录用户
        String tel = Utils.readCurrentUser(getContext());
        // 读取帐号信息
        if (tel != "") {
            imageButton.setVisibility(View.GONE);
            readDate(tel);
        } else {
            imageButton.setVisibility(View.VISIBLE);
        }

    }

    private void changeUI(List<String> info) {
        uName.setText(info.get(0));
        tvGender.setText(info.get(1));
        tvTel.setText(info.get(2));
        tvNickName.setText(info.get(3));
    }

    class Lister implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mine_btn_login:
                    Utils.showToast(getContext(), "登录");
                    // 打开登录界面并获取结果
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1);
                    // TODO 打开服务器连接
                   /*
                    mTcpClient = new TcpClient(mHandler,IP_ADDRESS,IP_PORT);
                    mTcpClient.setSendMsg("LOGIN::admin::123456");
                    mTcpClient.send();
                    */
                    break;

                case R.id.mine_btn_quit:
                    Utils.showToast(getContext(), "退出登录");
                    // 修改本地账户为"“
                    changeCurrentUser("");
                    tvNickName.setVisibility(View.GONE);
                    // 打开登录界面并获取结果
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1);
                    break;

                case R.id.mine_btn_addCar:

                    View view2 = View.inflate(getActivity(), R.layout.layout_mine_addcar, null);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("添加车辆")
                            .setView(view2)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText carNum = view2.findViewById(R.id.addCar_carNum);
                                    String s = carNum.getText().toString();
                                    Toast.makeText(getActivity(), "确定", Toast.LENGTH_SHORT).show();
                                    tvCarNum.setVisibility(View.VISIBLE);
                                    tvCarNum.setText(s);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    break;
                case R.id.mine_btn_changeInfo:
                    startActivity(new Intent(getActivity(),ChangeInfoActivity.class));
                    break;

                default:
                    break;
            }

        }
    }

    /**
     * 上一个活动结束时的回调函数
     * 取出返回结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("login_return");
                    Utils.showToast(getContext(), returnedData);
                    readDate(returnedData);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 从本地数据库中读取用户信息
     * tel: 当前登录帐号
     */
    private void readDate(String tel) {
        // 查询数据库中数据
        List<UsersInfo> userList = LitePal.where("tel = ?", tel).find(UsersInfo.class);
        // 查询到数据传递给handler进行UI更新
        infoList.add(userList.get(0).getName());
        infoList.add(userList.get(0).getGender());
        infoList.add(userList.get(0).getTel());
        infoList.add(userList.get(0).getNickName());

        Utils.sendMessage(mHandler, 3, "changeUI");
    }

    /**
     *  修改本地账户
     * */
    private void changeCurrentUser(String tel) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("tel",tel);
        editor.apply();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}