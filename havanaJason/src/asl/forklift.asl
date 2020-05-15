// Agent forklift in project havanaJason

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+youShouldBid : true 
    <- .print("Received bid request");
    do(bid);
    bid(X);
    .send(auctioner,tell,placeBid(X)).