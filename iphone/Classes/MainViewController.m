//
//  MainViewController.m
//  Session7
//
//  Created by Matthias Lübken on 13.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "MainViewController.h"
#import "S5URL.h"


@implementation MainViewController

NSString* ip;
NSString* port;
NSInteger numberofkeyframes;
S5URL* s5url;
UIInterfaceOrientation selectedInterfaceOrientation;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
		ip = @"localhost";
		port = @"8088";
    }
    return self;
}


- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation 
{
    return interfaceOrientation == UIInterfaceOrientationLandscapeLeft;
}

- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller showingAlertView: (UIAlertView*) alertView{
	numberofkeyframes = controller.numberofkeyframes;
	ip = controller.ip.text;
	//[ip retain];
	port = controller.port.text;
	//[port retain];
	if([controller.orientation selectedSegmentIndex] == 0) {//horizental
		selectedInterfaceOrientation = UIInterfaceOrientationLandscapeLeft;
	} else {//vertical
		selectedInterfaceOrientation = UIInterfaceOrientationPortrait;
	}
	s5url = [[S5URL alloc] initWithIp:controller.ip.text andPort:controller.port.text];
	[self dismissModalViewControllerAnimated:YES];

	[self.view setOrientation:[controller.orientation selectedSegmentIndex]];
	[self.view setNeedsLayout];
	
	[alertView dismissWithClickedButtonIndex:0 animated:YES];
}


- (IBAction)showInfo {    
	FlipsideViewController *controller = [[FlipsideViewController alloc] initWithNibName:@"FlipsideView" bundle:nil];
	controller.delegate = self;
	controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
	[self presentModalViewController:controller animated:YES];
	controller.port.text = port;
	controller.ip.text = ip;
	if(selectedInterfaceOrientation == UIInterfaceOrientationLandscapeLeft) {//horizental
		[controller.orientation setSelectedSegmentIndex:0];
	} else {//vertical
		[controller.orientation setSelectedSegmentIndex:1];
	}
	
	[controller release];
}


- (UIImage*) callForImage:(int)imageNr {   
	NSLog(@"callForImage %d", imageNr);
	NSData* data = [ NSData dataWithContentsOfURL: [s5url urlForImage:imageNr] ];
	if([data length] > 0) {
		//[self.imageView2.image release];
		return [ [ UIImage alloc ] initWithData: data ];
	}
	//dummy data
	return [UIImage imageNamed:[NSString stringWithFormat:@"folie%d.png", imageNr]];
	return nil;
	//[data autorelease];
}


/************************************************************************/
/*																		*/
/*	FlowCover Callbacks													*/
/*																		*/
/************************************************************************/

- (int)flowCoverNumberImages:(FlowCoverView *)view
{
	return numberofkeyframes;
}

- (UIImage *)flowCover:(FlowCoverView *)view cover:(int)image
{
	return [self callForImage:image];
}

- (void)flowCover:(FlowCoverView *)view didSelect:(int)image
{
	NSLog(@"Selected Index %d",image);
}

- (void)flowCover:(FlowCoverView *)view draggedTo:(int)image
{
	[s5url call:[NSString stringWithFormat:GO_TO, image] andDelegate:self];
}

- (void)dealloc {
    [super dealloc];
}


@end
