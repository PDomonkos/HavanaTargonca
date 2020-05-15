// Agent auctioner in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */

+startAuction : true <- .print("started").
+restart 
	: true 
	<- .print("restarted");
	   //+startAuction;
	   -restart.
	   

+createAgents
	 : true
	 <-
	 while(agentCount(X)[source(percept)]  & X >= 0) { 
       -+agentCount(X-1)[source(percept)];
	   .create_agent(Forklift, "forklift.asl");
     }.
