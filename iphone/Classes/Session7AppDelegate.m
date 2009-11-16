//
//  Session7AppDelegate.m
//  Session7
//
//  Created by Matthias Lübken on 13.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "Session7AppDelegate.h"
#import "MainViewController.h"
#import "Preferences.h"

@implementation Session7AppDelegate
Preferences* prefs1;

@synthesize window;
@synthesize mainViewController;


- (void)applicationDidFinishLaunching:(UIApplication *)application {
	
	[application setStatusBarStyle: UIStatusBarStyleBlackTranslucent];
	
	//Load prefs
	prefs1 = [[Preferences alloc] init];
	[prefs1 loadPrefs];
    
	MainViewController *aController = [[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
	self.mainViewController = aController;
	[self.mainViewController setPort:[prefs1 port]];
	[self.mainViewController setIp2:[prefs1 ip]];
	[aController release];
	
    mainViewController.view.frame = [UIScreen mainScreen].applicationFrame;
	[window addSubview:[mainViewController view]];
    [window makeKeyAndVisible];
	
	[aController showInfo];
}

- (void)applicationWillTerminate:(UIApplication *)application {
	[prefs1 setPort:[self.mainViewController port]];
	[prefs1 setIp:[self.mainViewController ip2]];

	[prefs1 savePrefs];
}


- (void)dealloc {
    [mainViewController release];
    [window release];
    [super dealloc];
}

@end
