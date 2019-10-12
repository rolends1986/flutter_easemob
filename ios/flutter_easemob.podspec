#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'flutter_easemob'
  s.version          = '0.0.1'
  s.summary          = 'easemob  flutter'
  s.description      = <<-DESC
easemob  flutter
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*','HelpDeskUI/HDUIKit/**/*','HelpDeskUI/**/*.h','HelpDeskUI/HelpDeskUI.m','HelpDeskUI/**/*.a'
  s.public_header_files = 'Classes/**/*.h'
  #,'HelpDeskUI/HelpDeskUI.h','HelpDeskUI/HDUIKit/ViewController/*.h','HelpDeskUI/HDUIKit/Views/**/*.h','HelpDeskUI/HDUIKit/Model/*.h','HelpDeskUI/HDUIKit/3rdparty/DeviceHelper/HDVoiceConvert/*.h','HelpDeskUI/HDUIKit/3rdparty/DeviceHelper/internal/*.h','HelpDeskUI/HDUIKit/3rdparty/DeviceHelper/delegates/*.h','HelpDeskUI/HDUIKit/Util/**/*.h','HelpDeskUI/HDUIKit/Helper/**/*.h','HelpDeskUI/HDUIKit/Add/**/*.h','HelpDeskUI/HDUIKit/Category/**/*.h','HelpDeskUI/HDUIKit/3rdparty/SDWebImage/**/*.h','HelpDeskUI/HDUIKit/3rdparty/Masonry/**/*.h','HelpDeskUI/HDUIKit/3rdparty/MWPhotoBrowser/**/*.h','HelpDeskUI/HDUIKit/3rdparty/MBProgressHUD/**/*.h'
  #s.private_header_files = 'HelpDeskUI/**/*.h' #
  s.prefix_header_contents = '#ifdef __OBJC__','#import <HelpDeskLite/HelpDeskLite.h>','#import "HelpDeskUI.h"','#endif'
  s.dependency 'Flutter'
  #s.xcconfig = { 'HEADER_SEARCH_PATHS' => '$(inherited)  $(PODS_TARGET_SRCROOT)/HelpDeskUI' }
  s.vendored_frameworks = 'sdk/HelpDeskLite.framework','sdk/HyphenateLite.framework'
  #s.vendored_libraries = 'HelpDeskUI/**/*.a'
  s.vendored_libraries = 'HelpDeskUI/libhdlibopencore-amrnb.a'#此处为神坑,lib的名称需为libxxx这样的文件名格式
  s.ios.deployment_target = '8.0'
  s.xcconfig = { 'OTHER_CFLAGS' => '$(inherited) -ObjC  ' }
  # -l$(PODS_TARGET_SRCROOT)/HelpDeskUI/HDUIKit/3rdparty/DeviceHelper/HDVoiceConvert/opencore-amrnb/hdlibopencore-amrnb.a -l$(PODS_TARGET_SRCROOT)/HelpDeskUI/HDUIKit/3rdparty/DeviceHelper/HDVoiceConvert/opencore-amrwb/hdlibopencore-amrwb.a
  s.pod_target_xcconfig = { 'OTHER_LDFLAGS' => '$(inherited) -lObjC' }
 
  s.resources= ['HelpDeskUI/resources/HelpDeskUIResource.bundle','HelpDeskUI/resources/newResources/*.png']
 
end

