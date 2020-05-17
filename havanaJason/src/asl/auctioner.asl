// Agent auctioner in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */
+restart : true <- .print("restarted"); -restart[source(percept)]; !deleteAgents. 

@pb0[atomic]
+end : true <- .abolish(szabad_a_tanc); .print("VEGE"); do(startEnv).

@pb1[atomic]
+!deleteAgents
	 : true
	 <-
	 .all_names(L);
	 for (.member(A,L)) {
		 if (not .substring(A, "auctioner")) {
			.print("KILLING AGENT", A);
			.kill_agent(A);
		 }
	 };
	 !createAgents.
	  
@pb2[atomic]
+!createAgents
	: true
	<- while(agentCount(X)[source(percept)]  & X > 0) { 
       -+agentCount(X-1)[source(percept)];
	   .create_agent(Forklift, "forklift.asl");
     }
	 +szabad_a_tanc;
	 !startAuction.
@pb3[atomic]
+!startAuction 
	: szabad_a_tanc 
	<- 
	.print("Starting auction");
	-+winner(none,99999999999); 
	-+votes(0);
	.broadcast(tell, youShouldBid).	
	
@pb4[atomic]
+placeBid(V)[source(S)] 
	: votes(K) & szabad_a_tanc
	<-
	-+votes(K+1);
	if (winner(CurWin, CurV) & V < CurV) {
		-winner(CurWin, CurV); 
		+winner(S, V); 
	};
	!checkEnd;
	-placeBid(V)[source(S)].

@pb5[atomic]
+!checkEnd 
	: winner(U, V) & votes(K) & .all_names(L) & .length(L, K+1)
	<- 
	.print("WINNER: ",U," with ",V); 
	.broadcast(tell, winner(U));
	.wait(400);
	!startAuction.
	
@pb6[atomic]
+!checkEnd 
	: true
	<- 
	.print("WAITING").
