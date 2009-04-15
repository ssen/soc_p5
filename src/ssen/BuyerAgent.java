package ssen;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class BuyerAgent extends Agent{

	protected void setup(){
		System.out.println("Setting Buyer Agent Starting");
		String buyer_arg0;
		String buyer_arg1;
		final String buyer_arg2;
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			buyer_arg0 = (String) args[0];
			buyer_arg1 = (String) args[1];
			buyer_arg2 = (String) args[2];
			addBehaviour(new RequestForQuote(buyer_arg2));
			
		}else {
			System.out.println("No Argument specified");
			doDelete();
		}
	}
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Buyer-agent "+getAID().getName()+" terminating.");
		}

}
