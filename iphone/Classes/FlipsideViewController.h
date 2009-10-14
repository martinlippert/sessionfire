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
	IBOutlet UISegmentedControl* orientation;
	NSInteger numberofkeyframes;
}

@property (nonatomic, retain) UITextField* ip;
@property (nonatomic, retain) UITextField* port;
@property (nonatomic, retain) UISegmentedControl* orientation;
@property (nonatomic) NSInteger numberofkeyframes;

@property (nonatomic, assign) id <FlipsideViewControllerDelegate> delegate;

- (IBAction)done;
- (IBAction)editingdoneIp;
- (IBAction)editingdonePort;
- (IBAction)sample;

@end


@protocol FlipsideViewControllerDelegate
- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller showingAlertView: (UIAlertView*) alertView;
@end

