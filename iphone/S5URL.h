//
//  S5Connection.h
//  Session7
//
//  Created by Matthias Lübken on 24.07.09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

//Available commands
#define NUMBER_OF_KEYFRAMES 	@"numberofkeyframes"
#define GO_TO			@"go?to=%d"
#define KEYFRAME_AT		@"keyframe?at=0%d"

@interface S5URL : NSObject {

}
- (id) initWithIp: (NSString*) ip1 andPort: (NSString*) port1;
- (NSURL*) urlFor: (NSString*) command;
- (NSURL*) urlForImage: (int) imageNr;
- (NSURLRequest*) requestFor: (NSString*) command;
- (NSURLConnection*) call: (NSString*) command andDelegate: (id) delegate;
@end
