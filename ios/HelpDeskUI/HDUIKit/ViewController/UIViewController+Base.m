//
//  UIViewController+BaseUIViewController.m
//  auto_orientation
//
//  Created by qinjilei on 2019/10/14.
//

#import "UIViewController+Base.h"
 

@implementation BaseViewController
- (void) setNaviBar:(BOOL)hidden{
    UIWindow *window=[[UIApplication sharedApplication] keyWindow];
    if(window){
           UIViewController *rootController=[window rootViewController];
           if(rootController && [rootController isKindOfClass:[UINavigationController class]]){
               [(UINavigationController*)rootController  setNavigationBarHidden:hidden];
               return;
           }
     }
}

- (void)viewDidLoad {
    [self setNaviBar:NO];
    [super viewDidLoad];
}
- (void)viewWillAppear:(BOOL)animated
{
    [self setNaviBar:NO];
    [super viewWillAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [self setNaviBar:YES];
    [super viewWillDisappear:animated];
}
@end
