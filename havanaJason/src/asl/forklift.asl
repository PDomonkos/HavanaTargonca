// Agent forklift in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+youShouldBid : true 
    <- .print("RECEIVED BID REQUEST");
    do(bid);
    -youShouldBid[source(auctioner)].
    
+bid(X) : true <- .print("SENDING BID",X); .send(auctioner, tell, placeBid(X)); -bid(X)[source(percept)].

+winner(U) : .my_name(U) <- do(win); -winner(U)[source(auctioner)].

+winner(U) : true <-  -winner(U)[source(auctioner)].
