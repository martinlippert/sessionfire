//
//  FlipsideViewController.h
//  Session5
//
//  Created by Matthias Lübken on 01.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

@protocol FlipsideViewControllerDelegate;


@interface FlipsideViewController : UIViewController {
	id <FlipsideViewControllerDelegate> delegate;
	IBOutlet UITextField* ip;
	IBOutlet UITextField* port;
	NSInteger numberofkeyframes;
}

@property (nonatomic, retain) UITextField* ip;
@property (nonatomic, retain) UITextField* port;
@property (nonatomic) NSInteger numberofkeyframes;


@property (nonatomic, assign) id <FlipsideViewControllerDelegate> delegate;
- (IBAction)done;

@end


@protocol FlipsideViewControllerDelegate
- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller showingAlertView: (UIAlertView*) alertView;
@end

