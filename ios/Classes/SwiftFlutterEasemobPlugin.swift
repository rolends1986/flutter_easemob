import Flutter
import UIKit
 

public class SwiftFlutterEasemobPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "vip.hsq168.plugin.flutter_easemob", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterEasemobPlugin()
    initSdk()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }
    
  static func initSdk(){
    let mainBundle = Bundle.main
    let info = mainBundle.infoDictionary!
    if(info.keys.contains("EasemobAppkey") && info.keys.contains("EasemobTenantId")){
        let appKey=info["EasemobAppkey"] as! String
        let tenantId=info["EasemobTenantId"] as! String
        let options=HDOptions()
        options.appkey=appKey
        options.tenantId=tenantId
        
        let err=HDClient.shared()?.initializeSDK(with: options)
        if(nil != err){
            NSLog("==========Easemob错误===============")
            NSLog("%@",err!.errorDescription)
        }
    }else{
        NSLog("===========请在plist填入EasemobAppkey和EasemobTenantId================")
    }
   
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if(call.method=="startServiceIM"){
        let dic=call.arguments as! NSDictionary
        startServiceIM(serviceNumber: dic.object(forKey: "serviceNumber") as! String)
    }
    result("iOS " + UIDevice.current.systemVersion)
  }
    
  func startServiceIM(serviceNumber:String) -> Void {
    var canLogin=false
    let client=HDClient.shared()
    if(!client!.isLoggedInBefore){
        let uuid = UUID().uuidString
        var userName = "guest"+uuid.replacingOccurrences(of: "-", with:"")
        userName=userName.substring(to:userName.index(userName.startIndex, offsetBy:16))
        var error=client!.register(withUsername: userName, password: userName)
        if(error==nil){
            canLogin=true
        }else{
            EWToast.showBottomWithText(text: "客服注册失败,请重试")
        }
        if(canLogin){
            error=client!.login(withUsername:userName, password: userName)
            if(error==nil){
                canLogin=true
            }else{
                EWToast.showBottomWithText(text: "客服登陆失败,请重试")
            }
        }
    }else{
         canLogin=true
    }
    if(canLogin){
        EMClient.shared()?.options.isAutoLogin=true;
        FlutterEasemobPlugin.startServiceIM(serviceNumber)
    }
    
  }
}
