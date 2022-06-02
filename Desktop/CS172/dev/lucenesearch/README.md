# Instructions 

Requriements: Java SE Development Kit 8u333 https://www.oracle.com/java/technologies/downloads/#jdk18-mac
            Apache Maven: https://maven.apache.org/download.cgi (tar or zip is ok)
            Visual Studio or other Java IDE

I have attached two articles below depending on your OS if you run into the mvn command not found error:
https://stackoverflow.com/questions/21028872/mvn-command-not-found-in-osx-mavrerick
https://stackoverflow.com/questions/10649707/maven-mvn-command-not-found

1. Download the lucenesearch zip and make a folder with that zip file and the apache maven file inside. Then unzip the file
2. Open the folder with your IDE, then open up a terminal window
3. In your terminal window press cd enter and then vim .bash_profile and add these lines below. Change the path names of course ;) 
    export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_333.jdk/Contents/Home/"
    export PATH=$PATH:$JAVA_HOME/bin
    export MAVEN_HOME="/Users/rohanbehera/Desktop/CS172/dev/apache-maven-3.8.5"
    export M2_HOME="/Users/rohanbehera/Desktop/CS172/dev/apache-maven-3.8.5"
    export MAVEN_HOME=$M2_HOME
    export M2=$M2_HOME/bin
    export MAVEN_OPTS="-Xms256m -Xmx512m"
    export PATH=$PATH:$MAVEN_HOME/bin
4. Type in source .bash_profile to save and then hit enter
5. Now run the command mvn and when thats done, cd into the folder you made in step 1 and run mvn clean install
6. Then run the command mvn spring-boot:run 
7. In your web browser log onto localhost:9090
8. Search for tweets!

