
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNImageFileTransformerSpec.h"

@interface ImageFileTransformer : NSObject <NativeImageFileTransformerSpec>
#else
#import <React/RCTBridgeModule.h>

@interface ImageFileTransformer : NSObject <RCTBridgeModule>
#endif

@end
