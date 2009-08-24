//
//  Preferences.m
//  Dishes
//
//  Created by Matthias Lübken on 09.03.09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "Preferences.h"


@implementation Preferences

NSString* prefsFilePath;
NSMutableDictionary *prefs;


- (void) initPrefsFilePath { 
	NSArray *paths = NSSearchPathForDirectoriesInDomains (NSDocumentDirectory, NSUserDomainMask, YES); 
	NSString *documentsDirectory = [paths objectAtIndex: 0]; 
	prefsFilePath = [documentsDirectory stringByAppendingPathComponent: @"sessionfireprefs.plist"]; 
	[prefsFilePath retain]; 
} 

- (void) loadPrefs { 
	if (prefsFilePath == nil) 
		[self initPrefsFilePath]; 
	if ([[NSFileManager defaultManager] fileExistsAtPath: prefsFilePath]) { 
		prefs = [[NSMutableDictionary alloc] initWithContentsOfFile: prefsFilePath]; 
		NSLog (@"read prefs from %@", prefsFilePath); 
	} 
	else { 
		NSLog (@"creating default prefs");
		prefs = [[NSMutableDictionary alloc] initWithCapacity: 2]; 
		[self setIp:@"localhost"];
		[self setPort:@"8088"];
	}
} 

- (void) savePrefs {
	// save prefs to documents folder 
	[prefs writeToFile: prefsFilePath atomically: YES]; 
	NSLog (@"saved prefs"); 
} 

- (NSString*) ip  {
	return [prefs objectForKey:@"IP"];
}
-(void) setIp:(NSString*) ip1 {
	[prefs setObject:ip1 forKey:@"IP"]; 
}
- (NSString*) port  {
	return [prefs objectForKey:@"PORT"];
}
-(void) setPort:(NSString*) port1 {
	[prefs setObject:port1 forKey:@"PORT"]; 
}
@end
