//
//  Session7AppDelegate.h
//  Session7
//
//  Created by Matthias Lübken on 13.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

@class MainViewController;

@interface Session7AppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    MainViewController *mainViewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) MainViewController *mainViewController;

@end

