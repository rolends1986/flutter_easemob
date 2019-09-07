#import "FlutterEasemobPlugin.h"
#import <flutter_easemob/flutter_easemob-Swift.h>

@implementation FlutterEasemobPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterEasemobPlugin registerWithRegistrar:registrar];
}
@end
