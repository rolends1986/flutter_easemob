import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    
  var flutterViewController: FlutterViewController?
  var naviController: UINavigationController?
    
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?
  ) -> Bool { 
//    self.flutterViewController = FlutterViewController()
//    self.naviController=UINavigationController.init(rootViewController:self.flutterViewController!)
//    self.naviController!.isNavigationBarHidden=true
//    self.window = UIWindow.init(frame: UIScreen.main.bounds)
//    self.window.rootViewController = self.naviController!
//    self.window.makeKeyAndVisible()
//    GeneratedPluginRegistrant.register(with: self)
//    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    
    //
      let flutterViewController: FlutterViewController = window?.rootViewController as! FlutterViewController
     //
      GeneratedPluginRegistrant.register(with: self)
      let navigationController = UINavigationController(rootViewController: flutterViewController)
      navigationController.isNavigationBarHidden = true
      window?.rootViewController = navigationController
     // mainCoordinator = AppCoordinator(navigationController: navigationController)
      window?.makeKeyAndVisible()
      //
      return super.application(application,     didFinishLaunchingWithOptions: launchOptions)
  }
    

    
//    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
//        super.touchesBegan(touches, with: event)
//
//        if let vc = UIApplication.shared.keyWindow?.rootViewController as? FlutterViewController {
//            return
//        }
//
//        self.flutterViewController?.handleStatusBarTouches(event)
//    }
//
//    override func registrar(forPlugin: String) -> FlutterPluginRegistrar {
//        return (self.flutterViewController?.registrar(forPlugin: forPlugin))!
//    }
//
//    override func hasPlugin(_ pluginKey: String) -> Bool {
//        return (self.flutterViewController?.hasPlugin(pluginKey))!
//    }
//
//    override func valuePublished(byPlugin pluginKey: String) -> NSObject {
//        return (self.flutterViewController?.valuePublished(byPlugin: pluginKey))!
//    }
}
