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
}

- (IBAction)showInfo;

@end
