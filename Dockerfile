FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY Test.java MyHandler.java HelloHandler.java ./
RUN javac *.java
EXPOSE 8000
CMD ["java", "Test"]
