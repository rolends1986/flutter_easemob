#import "FlutterEasemobPlugin.h"
#import <flutter_easemob/flutter_easemob-Swift.h>
#import "HelpDeskUI.h"


@implementation FlutterEasemobPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
     [[HDEmotionEscape sharedInstance] setEaseEmotionEscapePattern:@"\\[[^\\[\\]]{1,3}\\]"];
        [[HDEmotionEscape sharedInstance] setEaseEmotionEscapeDictionary:[HDConvertToCommonEmoticonsHelper emotionsDictionary]];
    [SwiftFlutterEasemobPlugin registerWithRegistrar:registrar];
}
 
+ (void)pushController:(UIViewController*)controller
{
    UIWindow *window=[[UIApplication sharedApplication] keyWindow];
    if(window){
        UIViewController *rootController=[window rootViewController];
        if(rootController && [rootController isKindOfClass:[UINavigationController class]]){
            [(UINavigationController*)rootController pushViewController:controller animated:YES];
            return;
        }
    }
}
+ (void)startServiceIM:(NSString*)serviceNumber{
 
      dispatch_async(dispatch_get_main_queue(), ^{
         HDMessageViewController *chatVC = [[HDMessageViewController alloc] initWithConversationChatter:serviceNumber];
         [self pushController:chatVC];
      });
}

@end
