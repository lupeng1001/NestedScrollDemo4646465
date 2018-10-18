package cn.tsou.constant;

/**
 * @author lupeng
 * @date 2018-10-18
 * 类说明 : 设备信息
 */
public class DeviceInfo {
  public static final int MACHINE_TYPE_ANDROID_PHONE = 1;
  public static final int SCREEN_TYPE_PHONE          = 1;
  public static final int SCREEN_TYPE_PAD            = 2;
  public int              screenWidth;                    // 屏幕宽
  public int              screenHeight;                   // 屏幕高
  public int              screenType;                     // 屏幕类型：1：手机，2：平板
  public int              machineType;                   // 手机版1 or 平板 2
  public int              sdkVersion;                     // sdk版本
  public String           cpuVersion;                     // cpu版本
  public String           manufacturer;                   // 厂商名称
  public String           model;                          // 机型
}
