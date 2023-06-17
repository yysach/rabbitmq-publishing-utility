# rabbitmq-publishing-utility
RabbitMq publishing utility with fanoutExchange.

# Requirements
Need java installed on the machine. jdk 1.8 or onwards

# Execution Steps:
Run: java -jar rabbitmq-publish-0.0.1-SNAPSHOT-jar-with-dependencies.jar -help

## Output
usage: java -jar rabbit-consumer-project-0.0.1-SNAPSHOT.jar
 -c,--createQueue <arg>    The declareQueue boolean flag. set true if want
                           to declare queue. Default:false
 -d,--dumpDataDir <arg>    The directory to dump data to. Default:
                           currentDirectory
 -h,--host <arg>           The hostName for rabbitmq server.
                           Default:localhost
 -help,--help              shows the help guide for the jar
 -p,--port <arg>           The port for the rabbitmq server to listen on.
                           Default:5672
 -q,--messageQueue <arg>   The messageQueue name to read data from.
                           Default:temp-queue
  
