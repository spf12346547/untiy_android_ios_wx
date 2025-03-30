package com.companyName.projectName;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.webkit.WebView;


public class PrivacyActivity extends Activity implements DialogInterface.OnClickListener {
    private static final String PREFS_NAME = "PlayerPrefs"; // SharedPreferences的文件名
    private static final String PRIVACY_ACCEPTED_KEY = "PrivacyAcceptedKey"; // 用于存储用户是否同意隐私政策的键

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 检查用户是否已经同意隐私政策
        if (getPrivacyAccept()) {
            // 如果已经同意，直接进入Unity主Activity
            enterUnityActivity();
            return;
        }
        // 如果未同意，显示隐私政策对话框
        showPrivacyDialog();
    }

    private String buildPrivacyHtml() {
        // 在这里设置隐私政策和用户协议的链接
        String userAgreementUrl = "https://docs.qq.com/doc/p/7e18456b6383b44b4a4c7d75011978896ea5e397?nlc=1";
        String privacyPolicyUrl = "https://docs.qq.com/doc/p/be89bb4ebb2b85b86b04eba1550befb63c4c056f?nlc=1";

        // 动态生成隐私政策的HTML内容
        return "欢迎使用本应用，在使用本应用前，请您充分阅读并理解 " +
                "<a href=\"" + userAgreementUrl + "\">《用户协议》</a>和" +
                "<a href=\"" + privacyPolicyUrl + "\">《隐私政策》</a>各条;" +
                "<br/>" +
                "1.保护用户隐私是本应用的一项基本政策，本应用不会泄露您的个人信息；" +
                "<br/>" +
                "2.我们会根据您使用的具体功能需要，收集必要的用户信息（如申请设备信息，存储等相关权限）；" +
                "<br/>" +
                "3.在您同意App隐私政策后，我们将进行集成SDK的初始化工作，会收集您的android_id、Mac地址、IMEI和应用安装列表，以保障App正常数据统计和安全风控；" +
                "<br/>" +
                "4.为了方便您的查阅，您可以通过“设置-关于我们”重新查看该协议；" +
                "<br/>" +
                "5.您可以阅读完整版的隐私保护政策了解我们申请使用相关权限的情况，以及对您个人隐私的保护措施。";
    }

    private void showPrivacyDialog() {
        // 创建一个WebView用于显示隐私政策的HTML内容
        WebView webView = new WebView(this);
        webView.loadData(buildPrivacyHtml(), "text/html", "utf-8");

        // 创建一个不可取消的对话框
        AlertDialog.Builder privacyDialog = new AlertDialog.Builder(this);
        privacyDialog.setCancelable(false);
        privacyDialog.setView(webView); // 设置WebView为对话框的内容视图
        privacyDialog.setTitle("提示"); // 设置对话框标题
        // 设置“拒绝”按钮，并指定点击事件监听器
        privacyDialog.setNegativeButton("拒绝", this);
        // 设置“同意”按钮，并指定点击事件监听器
        privacyDialog.setPositiveButton("同意", this);
        // 创建并显示对话框
        privacyDialog.create().show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        // 处理对话框按钮点击事件
        switch (i) {
            case AlertDialog.BUTTON_POSITIVE:
                // 如果点击了“同意”按钮
                setPrivacyAccept(true); // 保存用户同意隐私政策的状态
                enterUnityActivity(); // 进入Unity主Activity
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                // 如果点击了“拒绝”按钮
                finish(); // 结束当前Activity，退出应用
                break;
        }
    }

    private void enterUnityActivity() {
        // 创建一个Intent，用于启动Unity主Activity
        Intent unityAct = new Intent();
        unityAct.setClassName(this, "com.unity3d.player.UnityPlayerActivity");
        startActivity(unityAct);
    }

    private void setPrivacyAccept(boolean accepted) {
        // 使用SharedPreferences保存用户是否同意隐私政策的状态
        SharedPreferences.Editor prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        prefs.putBoolean(PRIVACY_ACCEPTED_KEY, accepted);
        prefs.apply(); // 提交更改
    }

    private boolean getPrivacyAccept() {
        // 从SharedPreferences中获取用户是否同意隐私政策的状态，默认为false
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(PRIVACY_ACCEPTED_KEY, false);
    }

}