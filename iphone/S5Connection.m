//
//  S5Connection.m
//  Session7
//
//  Created by Matthias Lübken on 24.07.09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "S5Connection.h"


@implementation S5Connection

NSString* ip;
NSString* port;

- (id) initWithIp: (NSString*) ip1 andPort: (NSString*) port1
{
	self = [super init];
	if (self != nil) {
		ip = ip1;
		port = port1;
	}
	return self;
}

- (NSURL*) url {
	return [ NSURL URLWithString: [NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/numberofkeyframes", ip, port] ];
}

- (NSURLRequest*) request {
	return [NSURLRequest requestWithURL:[self url]
							cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
						timeoutInterval:2.0];
}


@end
