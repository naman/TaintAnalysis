Run using proper Args from Run Config. Please see the screenshots for 2nd part as well.

@author Srishti Sengupta, 2013108.
@author Naman Gupta, 2013064.

Assumption
1. The local parameter name in the functions should be same as what we have in function invocation.
2. Storing only tainted return variables in every function. 
3. No variable should contain a dollar sign ($).
4. No assignment should be there in print statements.
5. A non library method invocation and a library method invocation cannot be present on the same line.
6. Only 1 method invocation on each line.
7. Methods with no arguments or untainted args will never return a tainted summary.
9. Max lines of code supported should be changed. 
