package ssen;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
public class SimpleAgent extends Agent {
		protected void setup() {
			// Printout a welcome message

				System.out.println("Hallo! Buyer-agent "+getAID().getName()+" is ready.");
				System.out.println("Hallo! Buyer-agent "+getAID()+" is ready.");
				Behaviour b;
				 	
				
				//doDelete();
		}
		protected void takeDown(){
			System.out.println("Good Bye Cruel World!!");
		}
		
}

