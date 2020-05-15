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
	-+winner(none,99999999999); 
	+votes(0);
	.broadcast(tell, youShouldBid).

@pb1[atomic]
+placeBid(V)[source(S)] 
	: votes(K)
	<-
	-+votes(K+1);
	if (winner(CurWin, CurV) & V < CurV) {
		-winner(CurWin, CurV); 
		+winner(S, V); 
	};
	!checkEnd.

@pb2[atomic]
+!checkEnd 
	: winner(U, V) & votes(K) & .all_names(L) & .length(L, K+1)
	<- 
	.print("WINNER: ",U," with ",V); 
	.broadcast(tell, winner(U)).
	
@pb3[atomic]
+!checkEnd 
	: true
	<- 
	.print("WAITING").
