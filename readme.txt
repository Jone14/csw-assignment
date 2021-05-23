#1) command to install maven if not already present

sudo apt install maven

#2) Command for Maven Build

mvn clean install

#3) Command to run app

java -jar csw-assignment-1.0.0-jar-with-dependencies.jar input-file.json output-file.xml
#java -jar csw-assignment-1.0.0-jar-with-dependencies.jar ../test-data/1.json ../output1.xml

mvn exec:java -Dexec.mainClass="com.csw.converters.ConverterFactory"