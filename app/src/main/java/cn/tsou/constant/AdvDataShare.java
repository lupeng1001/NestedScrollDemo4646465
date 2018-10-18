package cn.tsou.constant;


import java.util.List;

/**
 * 单例 数据共享
 *
 * @author 陆鹏
 */
public class AdvDataShare {
    public static String user_desc = "";

    public static String activity_tag = "";
    public static String sid = "1";
    public static final String app_id = "19";
    //public static final String jiami_key = "msPzebip94bfLETcnFK0";//新版本启用新的加密key

    //用于保存即时聊天状态(创客和创客聊天)
    public static String to_headimg = "";
    public static String to_nickname = "";
    public static String to_uid = "";
    public static String user_logo = "";
    //用于标注是否停留在系统消息界面
    public static String message_tag = "";
    public static String from_message_detail_tag = "";

    public static String mobile = "";//用户手机号码


    public static String TAG = "";
    public static String DEVICE_ID = "";
    //标注是横屏还是竖屏
    public static String clienttype = "0";//全局变量,标识是横屏还是竖屏  0竖屏 1横屏
    //即时消息通知
    public static String local_message_str = "";



    //定位部分常量数据保存
    public static String local_address = "";
    public static double latitude = 0.0d;//获取当前纬度
    public static double longtitude = 0.0d;//获取当前经度值
    public static String city_name = "";
    //是否开启推送
    public static String is_used_tuisong = "true";
    public static String userName;//登录成功返回username
    public static String nickname;//登陆成功返回昵称
    public static String passWord;
    public static String userperm = "";// 登录权限,暂时保留
    public static String mid_token = "";
    public static String cookie_value = "";// 登录的时候返回的cookie值,这里不需要了
    //websocket需要缓存的参数
    public static String shop_name = "";//店铺名称
    public static String shop_logo = "";//店铺logo
    //登录信息保存
    public static String userId = "";//用于保存mid,创客宝项目登录后保存的id
    public static String downkey = "";//下载加密key
    public static String spreadQrCode = "";//首页显示二维码
    public static String storetitle = "";//首页显示店铺名称
    //ws登录之后的用户id
    public static String uid = "";
    public static String chonglian_right_now = "true";
    //websocket连接需要用到的加密key(正式库和测试库统一)
    public static String message_jiami_key = "455sfhduDHT89mk0JSDF0HYhsGh008Yu";
    //设备屏幕基本信息
    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    public static int SCREEN_DENSITY_DPI = 0;

    //自然人申请多码付webview界面返回，需要刷新NewDuoMaFuShenQingActivity界面
    public static String need_fresh_ziranren_shenqing = "false";


//    //用于标注order_type
//    public static int ORDER_TYPE = 0;
//    public static int IS_ZHIFU_AGAIN = 0;//0代表第一次支付，1代表第二次支付
//    public static List<Payment> all_payment_list;


    //	本地测试库加密部分配置信息(最新2.0版本)
    public static String version_id = "11";
    public static String version_mini = "2.0";
    public static final String jiami_key = "95afac09c7cb742173f45e7c5e9faa53";
    public static String push_key = "1231516";




    //线上正式库加密部分配置信息(最新4.0版本)
    //正式库4.0版本  加密key:7a995472e26ecee7bb031ffd25777f37
//    public static String version_id = "59";
//    public static String version_mini = "6.0";
//    public static final String jiami_key = "df58c7a54cf0b4a73c7485d944fcfbf5";
//    public static String push_key = "1231516";


}
