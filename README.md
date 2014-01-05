Electronic Trading
========

The goal of this open source project is to build real-time and low latency electronic trading components in Java that can be used to build Exchanges/ECNs and Front Office Algo trading systems.  

System Diagram  
https://github.com/asim2025/etrading/blob/master/etrading/docs/Etrading%20Technical%20Design%20Diagram.jpeg


* Limit Order Book (LOB)  
  Maintain a list of unexecuted orders and match orders based on priority. Execute orders once matched.  

  Latency Targets: less than 1 ms on find/remove/accept/fill orders.
  Latency Current: approx 3-5 ms.


* Market Data  
  Investigate a free source to download historical intraday market data feed and replay it to reflect
  real-time market data conditions.

  Throughput Targets: 5 ticks per second

  A simple market data simulator built as a temporary workaround to generate random prices between a 
  range for each ticker.
  
  
* Exchange Gateway/Connectors  
  Support following FIX 4.2 connectivity from FO trading systems:
  * Pre-Trade : Market Data
  * Trade : Singe Order Handling (New/Cancel/Replace Order and Execution Report)
  * Post Trade: Allocation (w/ FIX 4.4 support)

  Latency Targets: less than 3 ms (excluding network latency).


* Algorithmic Trading  
  Use well known algorithms to generate orders.  Evaulate CEPs such as Esper to implement order generation requests.

  Latency Targets: less than 10 ms.

  Reference Book: Algorithmic Trading and DMA by Barry Johnson
  
  
 
 * Technical Strategies to be used to improve performance  
   * Data Structure  
     The order book uses a binary tree data structure to keep lookups to either O(1) or O(log n). 
     Avoid O(n) or O(n2) data structures.
   
   * Threading  
     Avoid multi-threading in core components if lock contention will degrade system performance. 
     Instead achieve concurrency by passing messages to objects that don't require synchronization.
   
   * Memory/Garbage Collection  
     Create object pools to avoid garbage collection of objects.
    
   * JVM Tunning  
     
   * LINUX Tunning  


