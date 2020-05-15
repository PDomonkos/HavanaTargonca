// Agent auctioner in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */


/* Plans */

+startAuction : true <- .print("started").
+restart 
	: true 
	<- .print("restarted");
	   //+startAuction;
	   //-+restart.
	   .

+createAgents
	 : true
	 <-
	 while(agentCount(X)  & X > 0) { 
       -+agentCount(X+1);
       .print(agentCount(X));
     }.