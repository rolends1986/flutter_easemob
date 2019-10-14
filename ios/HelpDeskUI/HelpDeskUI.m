/************************************************************
 *  * Hyphenate CONFIDENTIAL
 * __________________
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Hyphenate Inc.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Hyphenate Inc.
 */

#import "HelpDeskUI.h"

@implementation HelpDeskUI

+(NSString*)HDLocalizedString:(NSString*) key
                  withComment:(NSString*) comment{
    NSString* text=R_HDLocalizedString(key,comment);
    if(text!=nil&& text!=key){
        return text;
    }else{
        NSBundle* lang=   [NSBundle bundleWithPath:[[NSBundle mainBundle] pathForResource:[NSString stringWithFormat:@"%@/Lang",HDResourcePackage ] ofType:@"bundle"]];
        return [lang localizedStringForKey:(key) value:@"" table:nil];
    }
}
@end
