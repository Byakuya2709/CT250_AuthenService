# Sử dụng JDK 17 làm base image
FROM openjdk:17-jdk

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file JAR vào container
COPY target/auth-1.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8081 cho ứng dụng
EXPOSE 8081

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
