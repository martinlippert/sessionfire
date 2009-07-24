//
//  MainViewController.m
//  Session7
//
//  Created by Matthias Lübken on 13.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "MainViewController.h"
#import "MainView.h"
#import "S5URL.h"


@implementation MainViewController

NSString* ip;
NSString* port;
NSInteger numberofkeyframes;
S5URL* s5url;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}


- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation 
{
    return ((interfaceOrientation == UIInterfaceOrientationLandscapeLeft) ||
			(interfaceOrientation == UIInterfaceOrientationLandscapeRight));
}


- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller {
	numberofkeyframes = controller.numberofkeyframes;
	ip = controller.ip.text;
	[ip retain];
	port = controller.port.text;
	[port retain];
	
	s5url = [[S5URL alloc] initWithIp:controller.ip.text andPort:controller.port.text];
	[self dismissModalViewControllerAnimated:YES];
}


- (IBAction)showInfo {    
	
	FlipsideViewController *controller = [[FlipsideViewController alloc] initWithNibName:@"FlipsideView" bundle:nil];
	controller.delegate = self;
	
	controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
	[self presentModalViewController:controller animated:YES];
	
	[controller release];
}


- (UIImage*) callForImage:(int)imageNr {   
	NSLog(@"callForImage %d", imageNr);
	NSData* data = [ NSData dataWithContentsOfURL: [s5url urlForImage:imageNr] ];
	if([data length] > 0) {
		//[self.imageView2.image release];
		return [ [ UIImage alloc ] initWithData: data ];
	}
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
	[s5url call:[NSString stringWithFormat:@"go?to=%d", image] andDelegate:self];
}

- (void)dealloc {
    [super dealloc];
}


@end
