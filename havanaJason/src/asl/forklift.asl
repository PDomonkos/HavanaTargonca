// Agent forklift in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+youShouldBid : true 
    <- .print("RECEIVED BID REQUEST");
    do(bid).
    

+bid(X) : true <- .print("SENDING BID",X); .send(auctioner, tell, placeBid(X)).

+winner(U) : .my_name(U) <- do(win).
