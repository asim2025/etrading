etrading
========

Build electronic trading real-time and low latency components in Java.

* Order Matching Engine
  Build a fast limit order book. Execute orders once matched.
 
  latencies: 
  
  single stock 100k orders 
  accept order: < 3ms (target)
  find order: < 1ms (target)
  remove order: < 3ms (target)
  fill order: < 3ms (target)
 
 
* Algo Trader
  Use algorithms to generate orders.
 
  latencies:
  < 10 ms (target) / algo based
 
 
* Market Data Generator
  Tick level market data generator to test trading components.
  
  latencies:
  Two ticks per second per ticker.
  
  
 * Strategies to Performance
 
   Data Structure
   The order book uses a binary tree data structure to keep lookups to either O(1) or O(log n). 
   Avoid O(n) or O(n2) data structures.
   
   Threading
   Lock contention can decrease system performance so multithreading is not used. 
   The concurrency is achieved by passing messages to objects that don't require synchronization.
   
   Memory/Garbage Collection
   Create object pools to avoid garbage collection of objects.
   
   