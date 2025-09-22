FROM eclipse-temurin:17.jre-focal
WORKDIR /app
# COPY --from=build /app/target/Ecommerce-Monolito-0.0.1-SNAPSHOT.jar .
COPY --from=build target/Ecommerce-Monolito-0.0.1-SNAPSHOT.jar app.jar
# RUN ["java", "-jar", "Ecommerce-Monolito-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
USER 1001
ENTRYPOINT ["java", "-jar", "app.jar"]