package com.oobiliuoo.parkingtong.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.database.CarInfo;
import com.oobiliuoo.parkingtong.database.CarModelTable;
import com.oobiliuoo.parkingtong.database.UsersInfo;
import com.oobiliuoo.parkingtong.utils.ManagerActivity;
import com.oobiliuoo.parkingtong.utils.Utils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private ImageButton imageButton;
    private TextView uName;
    private TextView tvNickName;
    private TextView tvGender;
    private TextView tvTel;
    private ImageButton ibAddCar;
    private ImageView imageView;
    private Button btnQuit;
    private ConstraintLayout clChangeInfo;
    private ConstraintLayout clCarInfo;
    private LinearLayout llAddCar;
    private ImageButton ibSetting;

    private Handler mHandler;
    private List<String> infoList;

    private boolean isLogin = false;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MineFragment2() {
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
    public static MineFragment2 newInstance(String param1, String param2) {
        MineFragment2 fragment = new MineFragment2();
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
        return inflater.inflate(R.layout.fragment_mine_2, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {

       // initDate();

        infoList = new ArrayList<>();
        imageButton = getView().findViewById(R.id.mine_btn_login);
        imageButton.setOnClickListener(new Lister());

        imageView = getView().findViewById(R.id.mine_iv_1);

        btnQuit = getView().findViewById(R.id.mine_btn_quit);
        btnQuit.setOnClickListener(new Lister());

        uName = getView().findViewById(R.id.mine_tv_name);
        tvNickName = getView().findViewById(R.id.mine_tv_nickName);
        tvGender = getView().findViewById(R.id.mine_tv_gender);
        tvTel = getView().findViewById(R.id.mine_tv_tel);

        ibAddCar = getView().findViewById(R.id.mine_btn_addCar);
        ibAddCar.setOnClickListener(new Lister());

        clChangeInfo = getView().findViewById(R.id.mine_btn_changeInfo);
        clChangeInfo.setOnClickListener(new Lister());

        clCarInfo = getView().findViewById(R.id.mine_CL_carInfo);
        llAddCar = getView().findViewById(R.id.mine_ll_addCar);

        ibSetting = getView().findViewById(R.id.mine_ib_setting);
        ibSetting.setOnClickListener(new Lister());

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
                    case  4:
                        initCarInfo();
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
            imageView.setVisibility(View.VISIBLE);
            readDate(tel);
            if(!isLogin) {
                initCarInfo();
            }
            isLogin = true;
        } else {
            imageButton.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            clCarInfo.setVisibility(View.GONE);
            uName.setText("");
            tvGender.setText("");
            tvTel.setText("");
        }

    }

    private void initCarInfo() {

        List<CarInfo> car = LitePal.where("tel = ?", Utils.readCurrentUser(getContext())).find(CarInfo.class);
        if(car.size()>0) {
            clCarInfo.setVisibility(View.VISIBLE);
            TextView carNum = getActivity().findViewById(R.id.mine_tv_carNum);
            carNum.setText(car.get(0).getCarNum());
            TextView carName = getActivity().findViewById(R.id.mine_tv_carName);
            carName.setText(car.get(0).getCarName());
            TextView carModel = getActivity().findViewById(R.id.mine_tv_carModel);
            carModel.setText(car.get(0).getCarModel());
        }else {

            llAddCar.setVisibility(View.VISIBLE);
            clCarInfo.setVisibility(View.GONE);

            Utils.showToast(getContext(),"请先绑定车辆");
        }

    }

    private void initDate() {

        // 首次执行需要下面语句创建数据库
        SQLiteDatabase db = LitePal.getDatabase();

        String AODI_A8 = "奥迪A8";
        String[] AODI_A8_MODEL = {"2021款 A8L 50 TFSI quattro 舒适型"
                                    ,"2021款 A8L 50 TFSI quattro 豪华型"
                                        ,"2021款 A8L 55 TFSI quattro 尊贵型"};


        for(int i=0;i<3;i++) {
            CarModelTable carModel1 = new CarModelTable();
            carModel1.setCarName(AODI_A8);
            carModel1.setCarModel(AODI_A8_MODEL[i]);
            carModel1.save();
        }



        Utils.mLog1("MF","initDate ok");

    }

    private void changeUI(List<String> info) {
        tvNickName.setVisibility(View.VISIBLE);
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
                    isLogin = false;
                    tvNickName.setVisibility(View.GONE);
                    clCarInfo.setVisibility(View.GONE);
                    // 打开登录界面并获取结果
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1);
                    break;

                case R.id.mine_btn_addCar:

                    View view2 = View.inflate(getActivity(), R.layout.layout_mine_addcar, null);
                    Spinner carName = view2.findViewById(R.id.addCar_carName);
                    carName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            initCarModel(view2);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    new AlertDialog.Builder(getActivity())
                            .setTitle("添加车辆")
                            .setView(view2)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText carNum = view2.findViewById(R.id.addCar_carNum);
                                    String strCarNum = carNum.getText().toString();
                                    Spinner carName = view2.findViewById(R.id.addCar_carName);
                                    String strCarName = carName.getSelectedItem().toString();
                                    Spinner carModel = view2.findViewById(R.id.addCar_carModel);
                                    String strCarModel= carModel.getSelectedItem().toString();
                                    if(!"".equals(strCarNum)){
                                        List<CarInfo> carInfo = LitePal.where("carNum = ?", strCarNum).find(CarInfo.class);
                                        if(carInfo.size()==0) {
                                            CarInfo mCarInfo = new CarInfo();
                                            mCarInfo.setTel(Utils.readCurrentUser(getContext()));
                                            mCarInfo.setCarNum(strCarNum);
                                            mCarInfo.setCarName(strCarName);
                                            mCarInfo.setCarModel(strCarModel);
                                            mCarInfo.save();
                                            Utils.sendMessage(mHandler,4,"4");
                                            Toast.makeText(getActivity(), "确定", Toast.LENGTH_SHORT).show();
                                            llAddCar.setVisibility(View.GONE);
                                        }else {
                                            Toast.makeText(getActivity(), "该车牌已经绑定", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {

                                        Toast.makeText(getActivity(), "请输入车牌", Toast.LENGTH_SHORT).show();
                                    }


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

                case R.id.mine_ib_setting:

                    startActivity(new Intent(getActivity(), ManagerActivity.class));
                    break;
                default:
                    break;
            }

        }
    }



    public void initCarModel(View view){


        Spinner mCarName = view.findViewById(R.id.addCar_carName);

        String carName = mCarName.getSelectedItem().toString();

        List<CarModelTable> carModelList = LitePal.where("carName = ?", carName).find(CarModelTable.class);
        String[] mCarModel = new String[10];
        int i = 0;
        if(carModelList.size()>0){
            for(CarModelTable model : carModelList){
                mCarModel[i] = model.getCarModel();
                i++;
            }
        }

        Spinner carModel = view.findViewById(R.id.addCar_carModel);

        ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,mCarModel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carModel.setAdapter(adapter);

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