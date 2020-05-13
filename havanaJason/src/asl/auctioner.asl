// Agent auctioner in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */


/* Plans */

+startAuction : true <- .print("started").
+restart 
	: true 
	<- .print("restarted");
	   -+startAuction;
	   -restart.
