/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import java.util.ArrayList;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

public class Process { 		
	
	public static void main(String args[]) throws Exception{
		
		//Initialize Singleton of BidderGroup
		BidderGroup bg = new BidderGroup();	
		bg = BidderGroup.getInstance();
		
		//Initialize Strategies
    	Strategy AddictedBidderStrat = new AddictedBidderStrat();
    	Strategy CautiousBidderStrat = new CautiousBidderStrat();
    	Strategy WeakBidderStrat = new WeakBidderStrat();
    	
    	//Initialize Bidders
    	Bidder a = new Bidder("Josh", 4000, 1, AddictedBidderStrat);
    	Bidder b = new Bidder("Doug", 7700, 2, CautiousBidderStrat);
    	Bidder c = new Bidder("Chris", 18600, 3, WeakBidderStrat);
    	Bidder d = new Bidder("George", 3000, 1, CautiousBidderStrat);
    	Bidder e = new Bidder ("Edward", 9000, 2, AddictedBidderStrat);
    	Bidder f = new Bidder ("Francis", 15000, 3, WeakBidderStrat);

    	//Create ArrayList of Bidders
    	final ArrayList<Bidder> bidders = new ArrayList<Bidder>();
    	//Add bidders into list
    	bidders.add(a);
    	bidders.add(b);
    	bidders.add(c);
    	bidders.add(d);
    	bidders.add(e);
    	bidders.add(f);
    	
    	//Add the List of Bidders to the Singleton so we can maintain their amounts throughout lifecycle
    	bg.getBidderList(bidders);
    	
        // create CamelContext
        CamelContext context = new DefaultCamelContext();

        // connect to ActiveMQ JMS broker listening on localhost on port 61616
        ConnectionFactory connectionFactory = 
        	new ActiveMQConnectionFactory("tcp://localhost:62026");
        context.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        
        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure() {
            	// Separate topics based on Price Level
            	// Process each one with the SubProcess routine --> SUBPROCESS IS MAIN AUCTION METHOD
                from("jms:topic:MPCS_Auction_TOPIC_HIGH")
                .log("SUBSCRIBER RECEIVED: jms HIGH queue: ${body} from file: ${header.CamelFileNameOnly}")
                .process(new SubProcess(bidders))
                .to("jms:queue:MPCS_Auction_WINNER");
                
                from("jms:topic:MPCS_Auction_TOPIC_MID")
                .log("SUBSCRIBER RECEIVED: jms MID queue: ${body} from file: ${header.CamelFileNameOnly}")
                .process(new SubProcess(bidders))
                .to("jms:queue:MPCS_Auction_WINNER");
                
                from("jms:topic:MPCS_Auction_TOPIC_LOW")
                .log("SUBSCRIBER RECEIVED: jms LOW queue: ${body} from file: ${header.CamelFileNameOnly}")
                .process(new SubProcess(bidders))
                .to("jms:queue:MPCS_Auction_WINNER");
     
                try {
                	Thread.sleep(100);
                } catch (InterruptedException e) {
                	e.printStackTrace();
                }
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(15000);

        // stop the CamelContext
        context.stop();
    }
}
