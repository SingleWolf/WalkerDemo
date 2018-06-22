package com.walker.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.walker.R;
import com.walker.WalkerApplication;
import com.walker.base.BaseFragment;
import com.walker.data.sp.SecuredSPHelper;
import com.walker.utils.ToastUtil;

/**
 * summary :加密处理偏好存储
 * time    :2016/9/2 14:26
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class SecuredSPFragment extends BaseFragment {
    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        baseView.findViewById(R.id.btn_put_sp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        baseView.findViewById(R.id.btn_get_sp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecuredSPHelper securedSP = SecuredSPHelper.getSharedInstance(WalkerApplication.getContext());
                String info = String.format("姓 名:%s\n年龄:%d\n消费:%s\n性别:%s\n", securedSP.getString("name", null), securedSP.getInt("age", 0), String.valueOf(securedSP.getLong("total", (long) 0)), securedSP.getBoolean("male", false) ? "男" : "女").toString();
                new AlertDialog.Builder(getHoldActivity()).setMessage(info).setNegativeButton(android.R.string.ok, null).show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_secured_sp;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    /**
     * 显示输入框
     */
    private void showInputDialog() {
        final View inputView = LayoutInflater.from(getHoldActivity()).inflate(R.layout.layout_input_dialog_sp, null);
        new AlertDialog.Builder(getHoldActivity()).setTitle("完善信息").setIcon(
                android.R.drawable.ic_dialog_info).setView(inputView).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etName = (EditText) inputView.findViewById(R.id.et_name);
                EditText etAge = (EditText) inputView.findViewById(R.id.et_age);
                EditText etTotal = (EditText) inputView.findViewById(R.id.et_total);
                RadioButton rbMale = (RadioButton) inputView.findViewById(R.id.rb_male);
                String name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showShort("Name is null");
                    return;
                }
                String age = etAge.getText().toString().trim();
                if (TextUtils.isEmpty(age)) {
                    ToastUtil.showShort("Age is null");
                    return;
                }
                String total = etTotal.getText().toString().trim();
                if (TextUtils.isEmpty(total)) {
                    ToastUtil.showShort("Total is null");
                    return;
                }
                SecuredSPHelper securedSP = SecuredSPHelper.getSharedInstance(WalkerApplication.getContext());
                securedSP.edit().putString("name", name).apply();
                securedSP.edit().putInt("age", Integer.parseInt(age)).apply();
                securedSP.edit().putLong("total", Long.parseLong(total)).apply();
                securedSP.edit().putBoolean("male", rbMale.isChecked()).apply();
            }
        }).setNegativeButton(android.R.string.cancel, null).show();
    }

    /**
     * use the EncryptionManager to encrypt/decrypt data on your own
     */
    private void cryptOwnData() {
       /* EncryptionManager encryptionManager = new EncryptionManager(WalkerApplication.getContext(), getSharedPreferences("my_pref", MODE_PRIVATE));
        EncryptionManager.EncryptedData encryptedData = encryptionManager.encrypt(bytesToEncrypt);
        byte[] decryptedData = encryptionManager.decrypt(encryptedData);*/
    }
}
