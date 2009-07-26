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

- (NSURL*) urlFor: (NSString*) command {
	NSString* url = [NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/%@", 
					 ip, port, command];
	return [NSURL URLWithString:url];
}

- (NSURLRequest*) requestFor: (NSString*) command {
	return [NSURLRequest requestWithURL:[self urlFor:command]
							cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
						timeoutInterval:2.0];
}

- (NSURL*) urlForImage: (int) imageNr {
	NSString* command = [NSString stringWithFormat:KEYFRAME_AT, imageNr];
	return [self urlFor:command];
}


- (NSURLConnection*) call: (NSString*) command andDelegate: (id) delegate {
	NSURLRequest *theRequest =	[self requestFor:command];	
	NSURLConnection *theConnection = [[NSURLConnection alloc] initWithRequest:theRequest delegate:delegate];
	return theConnection;
}



@end
