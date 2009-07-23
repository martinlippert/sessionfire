//
//  FlipsideViewController.m
//  Session5
//
//  Created by Matthias Lübken on 01.07.09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "FlipsideViewController.h"


@implementation FlipsideViewController

@synthesize delegate, ip, port, image;

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor viewFlipsideBackgroundColor];      
}


- (IBAction)done {
	/*
	 UIActivityIndicatorView* view = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge]; 
	 [view startAnimating];
	 [view setFrame:CGRectMake(120, 50, 40, 40)];
	 alertVerify = [[UIAlertView alloc] initWithTitle:@"Connecting..." 
	 message:nil
	 delegate:nil cancelButtonTitle:nil otherButtonTitles:nil, nil];
	 [alertVerify addSubview:view];
	 [alertVerify show];
	 [alertVerify dismissWithClickedButtonIndex:0 animated:NO];
	 UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Verification succeeded" 
	 message:nil
	 delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
	 [alert show];
	 [alert release];
	 */
//	NSString* url= [NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/images", ip.text, port.text];
	NSURL *url = [ NSURL URLWithString: [NSString stringWithFormat:@"http://%@:%@/sessionfive/remotecontrol/images", ip.text, port.text] ];
	NSLog(@"%@", url);
	self.image = [ [ UIImage alloc ] initWithData: [ NSData dataWithContentsOfURL: url ] ];

	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection succeeded" 
													message:nil
												   delegate:self 
										  cancelButtonTitle:@"OK" 
										  otherButtonTitles:nil, nil];
	[alert show];
	[alert release];
	/*
	NSURLRequest *theRequest = [NSURLRequest requestWithURL:[NSURL URLWithString:url]
											  cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
										  timeoutInterval:5.0];

	NSURLConnection *theConnection = [[NSURLConnection alloc] initWithRequest:theRequest delegate:self];
	*/
}



// Only landscape.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return (interfaceOrientation == UIInterfaceOrientationLandscapeLeft || interfaceOrientation == UIInterfaceOrientationLandscapeRight) ;
}


- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}

#pragma mark -
#pragma mark NSURLConnection delegate
- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection succeeded" 
													message:nil
												   delegate:self 
										  cancelButtonTitle:@"OK" 
										  otherButtonTitles:nil, nil];
	[alert show];
	[alert release];
}

- (void)connection:(NSURLConnection *)connection 
	didReceiveData:(NSData *)data { 
	NSLog (@"connectionDidReceiveData"); 
	NSString *newText = [[NSString alloc] 
						 initWithData:data 
						 encoding:NSUTF8StringEncoding]; 
	if (newText != NULL) { 
		[self appendTextToView:newText]; 
		[newText release]; 
	} 
} 

- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error
{
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection failed!" 
													message:nil
												   delegate:self 
										  cancelButtonTitle:@"OK" 
										  otherButtonTitles:nil, nil];
	[alert show];
	[alert release];
}
#pragma mark -
#pragma mark UIAlertView 
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	[self.delegate flipsideViewControllerDidFinish:self];		
}


@end
