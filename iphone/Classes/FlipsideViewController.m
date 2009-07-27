//
//  FlipsideViewController.m
//  Session5
//
//  Created by Matthias Lübken on 01.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "FlipsideViewController.h"
#import "S5URL.h"



@implementation FlipsideViewController

@synthesize delegate, ip, port, numberofkeyframes;

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor viewFlipsideBackgroundColor];      
}


- (IBAction)done {
	S5URL* s5url = [[S5URL alloc] initWithIp:ip.text andPort:port.text];
	//start connection
	[[NSURLConnection alloc] initWithRequest:[s5url requestFor:NUMBER_OF_KEYFRAMES] delegate:self];
}
- (IBAction)editingdone {
	NSLog(@"test");
}



// Only landscape.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return (interfaceOrientation == UIInterfaceOrientationLandscapeLeft || interfaceOrientation == UIInterfaceOrientationLandscapeRight) ;
}


- (void)dealloc {
    [super dealloc];
}

#pragma mark -
#pragma mark NSURLConnection delegate
- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{	
	UIActivityIndicatorView* view = [[UIActivityIndicatorView alloc] 
									 initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge]; 
	[view startAnimating];
	[view setFrame:CGRectMake(120, 45, 50, 50)];
	UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"Loading presentation." 
														 message:nil
														delegate:nil 
											   cancelButtonTitle:nil 
											   otherButtonTitles:nil, nil];
	[alertView addSubview:view];
	[alertView show];
	[self.delegate flipsideViewControllerDidFinish:self showingAlertView:alertView];	
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data { 
	NSLog (@"connectionDidReceiveData"); 
    NSString *numberofkeyframesS = [[NSString alloc] initWithData:data  encoding: NSASCIIStringEncoding];
	numberofkeyframes = [numberofkeyframesS integerValue];
} 

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection failed!" 
													message:nil
												   delegate:self 
										  cancelButtonTitle:@"OK" 
										  otherButtonTitles:nil, nil];
	[alert show];
	[alert release];
}



@end
