//
//  S5Connection.m
//  Session7
//
//  Created by Matthias Lübken on 24.07.09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "S5URL.h"


@implementation S5URL

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
	return [NSURL URLWithString: 
			[NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/numberofkeyframes", 
			 ip, port]];
}

- (NSURL*) urlForImage: (int) imageNr {
		return [ NSURL URLWithString: [NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/keyframe?at=0%d", ip, port, imageNr] ];
}

- (NSURLRequest*) request {
	return [NSURLRequest requestWithURL:[self url]
							cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
						timeoutInterval:2.0];
}

- (NSURLConnection*) call: (NSString*) command andDelegate: (id) delegate {
	NSString* url= [NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/%@", ip, port, command];
	
	NSLog(@"Calling %@", url);
	NSURLRequest *theRequest = [NSURLRequest requestWithURL:[NSURL URLWithString:url]
												cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
											timeoutInterval:5.0];
	
	NSURLConnection *theConnection = [[NSURLConnection alloc] initWithRequest:theRequest delegate:delegate];
	return theConnection;
}



@end
