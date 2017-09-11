cd %~dp0

call mvnw clean install

cd target

java -jar cashman-infosys-0.0.1-SNAPSHOT.jar

cmd /k



