# User Service V1.0 Deprecated

I believe it is worth keeping a record of this project and also have a conversasion about why I chose to do so in the first place. There are a number of concerns that led to this version's deprecation. 

Below are some of the key security and technical reasons I will be discussing:

- Security
  - Dealing with user data on me own (theory) 
  - Passing around sensitive user data, even hashed is not enough 
  - Static analysis issues (SQL injection)
- Technical
  - Already existing solutions (oauth2)
  - Use of custom persistence despite existing dedicated (and tested) plugins (JDC)
- Takeaways
  - Learned static analysis
  - Learned CI-CD
  - Learned popular framework
  - Made an API
- Conclusion
  - Make use of JDC and other plugins to solve issues
  - Make use of oauth2


Work with others!
