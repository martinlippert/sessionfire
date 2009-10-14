//
//  FlipsideViewController.m
//  Session5
//
//  Created by Matthias Lübken on 01.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "FlipsideViewController.h"
#import "S5URL.h"
#import "Reachability.h"


@implementation FlipsideViewController

@synthesize delegate, ip, port, numberofkeyframes, orientation;
BOOL dialogHasBeenShown = NO;

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor viewFlipsideBackgroundColor];      

}

- (void)viewWillAppear:(BOOL)animated {
	[ip becomeFirstResponder];
}

- (void)viewDidAppear:(BOOL)animated {
	if(!dialogHasBeenShown){
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Check your settings" 
												message:@"We encourage to turn on Airplane mode and connect your iPhone via an Adhoc network to your computer."
											   delegate:self 
									  cancelButtonTitle:@"OK" 
									  otherButtonTitles:nil, nil];
		[alert show];
		[alert release];
		dialogHasBeenShown = YES;
	}
}

- (IBAction)done {
	//test reachability
	Reachability* reachability = [Reachability sharedReachability];
	if ([[NSPredicate predicateWithFormat: @"SELF MATCHES '[a-z]+'"] evaluateWithObject:ip.text ] == YES) {
		[reachability setHostName:ip.text];
	}
	else{
		[reachability setAddress:ip.text];
	}
	NetworkStatus status = [reachability remoteHostStatus];
	if(status == NotReachable || status == ReachableViaCarrierDataNetwork) {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No Wifi" 
														message:@"Please ensure your Wifi is turned on."
													   delegate:self 
											  cancelButtonTitle:@"OK" 
											  otherButtonTitles:nil, nil];
		[alert show];
		[alert release];	
	} else {
		S5URL* s5url = [[S5URL alloc] initWithIp:ip.text andPort:port.text];
		//start connection
		[[NSURLConnection alloc] initWithRequest:[s5url requestFor:NUMBER_OF_KEYFRAMES] delegate:self];
	}	
}

- (IBAction)sample {
	numberofkeyframes = 9;
	[self.delegate flipsideViewControllerDidFinish:self showingAlertView:nil];	
}

- (IBAction)editingdoneIp {
	[port becomeFirstResponder];
}
- (IBAction)editingdonePort {
	[ip becomeFirstResponder];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (void)dealloc {
    [super dealloc];
}

#pragma mark -
#pragma mark NSURLConnection delegate
- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{	
	if(numberofkeyframes > 0) {
		[self.delegate flipsideViewControllerDidFinish:self showingAlertView:nil];	
	} else {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No presentation" 
														message:@"Please open a presentation on the desktop"
													   delegate:self 
											  cancelButtonTitle:@"OK" 
											  otherButtonTitles:nil, nil];
		[alert show];
		[alert release];
	}
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data { 
	NSLog (@"connectionDidReceiveData"); 
    NSString *numberofkeyframesS = [[NSString alloc] initWithData:data  encoding: NSASCIIStringEncoding];
	numberofkeyframes = [numberofkeyframesS integerValue];
} 

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection failed" 
													message:@"Couldn't find Sessionfire desktop."
												   delegate:self 
										  cancelButtonTitle:@"OK" 
										  otherButtonTitles:nil, nil];
	[alert show];
	[alert release];
}



@end
