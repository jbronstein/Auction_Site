Josh Bronstein

Project: Auction

To Run Correctly: There are three projects, you need to first play the “MPCS-Auction-ProducerItems” project. Then you need to play the “MPCS-Auction-Process” Project. Finally, play the “MPCS-Auction-ConsumeItems” project. These need to play consecutively in the order described similar to how we did Lab 7.

General Breakdown: My auction produces a Queue of Auction items with price and item rank (each item is HIGH value, MID value, or LOW value) through my ProducerItems class to the MPCS_Auction_ProducerItems queue. I then parse these items into topics based upon their item rank (HIGH, MID, or LOW) through the ConsumeItems class(NOTE: This still needs to be run last to properly work). Finally, my Processor (MPCS-Auction-Process) takes items from the topics and separates them into independent auctions and declares a winner through it’s SubProcessor (SubProcess class). It then directs a message regarding the Item and the Item’s winning bidder to a MPCS_Auction_WINNER queue. The majority of my code/auction takes place in the Process project, including my major design patterns.

Process Class: Process instantiates a singleton of the BidderGroup to keep track of which bidders are in the auction and how much cash they have left. It also sets up their strategies and other general data. It then passes this information to the SubProcess routines which pull items based on their Topic item value (HIGH, MID, LOW). In the SubProcess Class, I run all of my Independent Auction systems including who is bidding, what bid they place and who wins that auction. I created a BidderIterator to iterate through ArrayList of Bidders within the SubProcess Class, this helped set up which specific bidders would be placed into each auction. Generally, I tried to keep “high rollers” to higher value items (MID and HIGH), and “low rollers” to “LOW” value items. Each bidder has one of three strategies, which I just populated with some random function values to generate a bid offer. To get each auction winner, I have a helper method which takes in the initial price of the item, iterates through the bidders involved in that auction, and sees how much they offered for the item based on their strategy/current amount. Whoever provides the highest offer wins the bid. I enclose the winner within a new message and direct it to the MPCS_Auction_WINNER queue. 

Design Pattern Navigation:

Singleton -> Within MPCS_Auction_Process project. BidderGroup.java is the Singleton, it is called within the Process.java class at the beginning to keep track of the bidders throughout the auction.

Strategy -> Within MPCS_Auction_Process project. Strategy initialized at start of Process.java and assigned to each bidder as a Bidder. Three Strategy types within the class and these are called when receiving a bidder’s bid offer.

Iterator -> Within MPCS_Auction_Process project. BidderIterator.java and Iterator.java. Used in the SubProcess class and specifically within the findEntryBidders method, which takes in an ArrayList of all auction bidders, and determines who enters each auction.

Message Queue -> my MPCS_Auction_ProducerItems project produces a Queue of Auction Items and my MPCS_Auction_ConsumeItems project dequeues these items and enqueues them to the correct topic through parsing. Then these topics are dequeued by the Processor.

Pipes and Filter -> General Principles uses throughout my Project. When an auction item is taken from the topics, it its filtered into the correct SubProcess routine by the Process aggregator and filtered further by finding the correct Auction Bidders to incorporate into it’s bidding process before “cleaning” the order and finding the Winning Bidder for the item. It then Sends this item to the Winner Queue.

EIP Pattern Navigation:

A couple of these are pretty obviously used within my system by default, i.e. Message Broker and Message, because I’m brokering messages between my programs and the ActiveMQ application. 

Content-Based Router -> ConsumerItems project takes items from the MPCS_Auction_ProducerItems queue and routes them to the correct topic based on their auction rank (HIGH, MID, LOW)

Pub-Sub -> Consumer has three topics and the Processor takes in items from these topics and creates a SubProcessor that only subscribes to that topic.

Message Endpoint: Messages sent from ProducerItems to the Queue where ActiveMQ is the ReceiverApplication. 

Message Router -> Process class is a Message Router, which routes each Auction Item message to the correct SubProcess routine.




