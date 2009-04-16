package ssen;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class SellerAgent extends Agent{
	private MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			
	protected void setup(){
		System.out.println("Setting Seller Agent Starting");
		String argSeller;
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			argSeller = (String) args[0];
			addBehaviour(new SellerBehaviour());
			
		}else {
			System.out.println("No Argument specified");
		}
	}

	private void register() {
		DFAgentDescription dfd = new DFAgentDescription(); 
		ServiceDescription descriptionOfMyService = new ServiceDescription();
		dfd.setName(getAID()); 
		descriptionOfMyService.setType("SELLER");
		dfd.addServices(descriptionOfMyService);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
