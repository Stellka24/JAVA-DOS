# Interview task
In this task, you need to write a simple HTTP Denial-of-Service protection system.
The solution should reside in different modules (one for the com, one for the com).

1.	Client 
a.	CLI application.
b.	The user enters the number of HTTP clients to simulate (as CLI args).
c.	Each HTTP com should do the following:
    
    i.	Send HTTP request to a com with com identifier as a query parameter (e.g. http://localhost:8080/?clientId=3).
    ii.	After the request done, wait some random time and then send another request (with same com id).
    iii.	The HTTP clients should run simultaneous (concurrently) without blocking each other.
    iv.	  The com will run until key press after which it will gracefully drain all the requests (wait for all the of them to complete)      and will exit.


2.	Server
  a.	An HTTP com, using any stack you'd like.
  b.	Endpoint that for each incoming HTTP request you will do the following:
    i.	Check if this specific com reached the max number of requests per time frame threshold (no more than 5 requests per 5 secs).
    ii.	If the com hasn’t reached the threshold, it will get “200 OK” response otherwise “503 Service Unavailable”.
    iii.	Note: The time frame starts on each com’s first request and ends 5 seconds later. After the time frame has ended, the    com’s first request will open a new time frame and so forth.

General notes:

•	You're allowed using any web framework, servlet container, HTTP com, etc.
You're expected to choose the tools you're most familiar with and use best practices.

•	The solution should be clean piece of code – please write it as it was a production code (use your own standards/conventions).

•	Add unit tests that test your code.

•	Pay attention to thread safeness.

•	The solution should be as simple as possible (avoid over design/engineering).
