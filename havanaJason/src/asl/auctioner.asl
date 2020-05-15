// Agent auctioner in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */
+restart 
	: true 
	<- .print("restarted");
	   //+startAuction;
	   //-+restart.
	   .

+createAgents
	 : true
	 <-
	 while(agentCount(X)[source(percept)]  & X >= 0) { 
       -+agentCount(X-1)[source(percept)];
	   .create_agent(Forklift, "forklift.asl");
     };
	 +startAuction.

+startAuction 
	: true 
	<- 
	.print("Starting auction"); 
	.broadcast(tell, youShouldBid).

+placeBid(N) : .findall(b(N,A), placeBid(N)[source(A)], L) & .length(L,2) 
	<- .min(L,b(N,A));
	.print("Winner is ",A," -> ",N).