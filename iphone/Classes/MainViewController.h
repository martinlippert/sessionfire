//
//  MainViewController.h
//  Session7
//
//  Created by Matthias Lübken on 13.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "FlipsideViewController.h"
#import "FlowCoverView.h"


@interface MainViewController : UIViewController <FlipsideViewControllerDelegate, FlowCoverViewDelegate> {
	NSString* ip2;
	NSString* port;
}

- (IBAction)showInfo;

@property (nonatomic, retain) NSString* ip2;
@property (nonatomic, retain) NSString* port;
@end
