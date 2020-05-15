// Agent auctioner in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */
+restart : true <- .print("restarted"); -restart[source(percept)]; !createAgents; !startAuction.

+!createAgents
	 : true
	 <-
	 .all_names(L);
	 for (.member(A,L)) {
		 if (not .substring(A, "auctioner")) {
			.print("KILLING AGENT", A);
			.kill_agent(A);
		 }
	 };
	 while(agentCount(X)[source(percept)]  & X > 0) { 
       -+agentCount(X-1)[source(percept)];
	   .create_agent(Forklift, "forklift.asl");
     }.

+!startAuction 
	: true 
	<- 
	.print("Starting auction"); 
	.broadcast(tell, youShouldBid).

+placeBid(N) : .all_names(Q) & .length(Q, QL) & .findall(b(N,A), placeBid(N)[source(A)], L) & .length(L,QL-1) 
	<- 
	.min(L,b(N,A));
	.print("Winner is ",A," -> ",N).