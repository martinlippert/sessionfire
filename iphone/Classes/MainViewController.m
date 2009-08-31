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

@synthesize ip2, port;

NSInteger numberofkeyframes;
S5URL* s5url;
UIInterfaceOrientation selectedInterfaceOrientation;
UIAlertView* progressAlert;
UIProgressView* progressView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
    }
    return self;
}


- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation 
{
    return interfaceOrientation == UIInterfaceOrientationLandscapeLeft;
}

- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller showingAlertView: (UIAlertView*) alertView{
	progressAlert = [[UIAlertView alloc] initWithTitle: @"Loading"
											   message: @"message"
											  delegate: self
									 cancelButtonTitle: nil
									 otherButtonTitles: nil];
    progressView = [[UIProgressView alloc] initWithFrame:CGRectMake(30.0f, 80.0f, 225.0f, 90.0f)];
	[progressView setProgressViewStyle:UIProgressViewStyleDefault];
    [progressAlert addSubview:progressView];
	[progressAlert show];
	
	numberofkeyframes = controller.numberofkeyframes +1;//first overview page
	ip2 = controller.ip.text;
	[ip2 retain];
	port = controller.port.text;
	[port retain];
	if([controller.orientation selectedSegmentIndex] == 0) {//horizental
		selectedInterfaceOrientation = UIInterfaceOrientationLandscapeLeft;
	} else {//vertical
		selectedInterfaceOrientation = UIInterfaceOrientationPortrait;
	}
	s5url = [[S5URL alloc] initWithIp:controller.ip.text andPort:controller.port.text];
	[self dismissModalViewControllerAnimated:YES];

	[(FlowCoverView*)self.view setOrientation:[controller.orientation selectedSegmentIndex]];

	[self.view setNeedsLayout];
	
	[progressAlert dismissWithClickedButtonIndex:0 animated:YES];
}


- (IBAction)showInfo {    
	FlipsideViewController *controller = [[FlipsideViewController alloc] initWithNibName:@"FlipsideView" bundle:nil];
	controller.delegate = self;
	controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
	[self presentModalViewController:controller animated:YES];
	[controller.port setText:[self port]];
	[controller.ip setText:[self ip2]];
	if(selectedInterfaceOrientation == UIInterfaceOrientationLandscapeLeft) {//horizental
		[controller.orientation setSelectedSegmentIndex:0];
	} else {//vertical
		[controller.orientation setSelectedSegmentIndex:1];
	}
	
	[controller release];
}


- (UIImage*) callForImage:(int)imageNr {   
	NSLog(@"callForImage %d", imageNr);
	if(imageNr == 0) {
		return  [UIImage imageNamed:@"start.png"];		
	} else {
		NSData* data = [ NSData dataWithContentsOfURL: [s5url urlForImage:imageNr-1] ];
		if([data length] > 0) {
			//[self.imageView2.image release];
			return [ [ UIImage alloc ] initWithData: data ];
		}
		return nil;		
	}
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

- (void) updateProgressView:(NSNumber*) image
{
	//TODO klären ob das richtig ist
	NSAutoreleasePool* pool = [[NSAutoreleasePool alloc]init];
	[progressAlert setMessage:[NSString stringWithFormat:@"Image number %d",[image intValue]]];
	float progress = ((float)[image intValue] / (float)numberofkeyframes);
	[progressView setProgress:  1 - progress];
	[pool release];
}

- (UIImage *)flowCover:(FlowCoverView *)view cover:(int)image
{
	NSNumber* number = [NSNumber numberWithInt:image];
	[self performSelectorInBackground:@selector(updateProgressView:) withObject:number];
	return [self callForImage:image];
}


- (void)flowCover:(FlowCoverView *)view didSelect:(int)image
{
	NSLog(@"Selected Index %d",image);
}

- (void)flowCover:(FlowCoverView *)view draggedTo:(int)image
{
	[s5url call:[NSString stringWithFormat:GO_TO, image-1] andDelegate:self];
}

- (void)dealloc {
    [super dealloc];
}


@end
