//
//  Preferences.h
//  Dishes
//
//  Created by Matthias Lübken on 09.03.09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Preferences : NSObject {
}

- (void) loadPrefs;
- (void) savePrefs;
- (NSString*) ip;
-(void) setIp:(NSString*) ip1;
- (NSString*) port;
-(void) setPort:(NSString*) port1;

@end
