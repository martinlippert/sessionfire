//
//  MainViewController.m
//  Session7
//
//  Created by Matthias Lübken on 13.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "MainViewController.h"
#import "MainView.h"


@implementation MainViewController

NSString* ip;
NSString* port;
NSInteger numberofkeyframes;

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
	[self dismissModalViewControllerAnimated:YES];
}


- (IBAction)showInfo {    
	
	FlipsideViewController *controller = [[FlipsideViewController alloc] initWithNibName:@"FlipsideView" bundle:nil];
	controller.delegate = self;
	
	controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
	[self presentModalViewController:controller animated:YES];
	
	[controller release];
}

- (void)call:(NSString *)command {    
	
	NSString* url= [NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/%@", ip, port, command];
	
	NSLog(@"Calling %@", url);
	NSURLRequest *theRequest = [NSURLRequest requestWithURL:[NSURL URLWithString:url]
												cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
											timeoutInterval:5.0];
	
	NSURLConnection *theConnection = [[NSURLConnection alloc] initWithRequest:theRequest delegate:self];
}

- (UIImage*) callForImage:(int)whichImage {    
	NSURL *url2 = [ NSURL URLWithString: [NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/keyframe?at=0%d", ip, port, whichImage] ];
	NSData* data = [ NSData dataWithContentsOfURL: url2 ];
	NSLog(@"callForImage %d", whichImage );
	if([data length] > 0) {
		//[self.imageView2.image release];
		return [ [ UIImage alloc ] initWithData: data ];
	}
	return nil;
	//[data autorelease];
}

/*
 // Override to allow orientations other than the default portrait orientation.
 - (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
 // Return YES for supported orientations
 return (interfaceOrientation == UIInterfaceOrientationPortrait);
 }
 */

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
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
	[self call:[NSString stringWithFormat:@"go?to=%d", image]];
}

- (void)dealloc {
    [super dealloc];
}


@end
