FROM openjdk:21-jdk-slim
# Thiết lập đường dẫn làm việc trong container
WORKDIR /app

# Sử dụng ARG để truyền file JAR
ARG JAR_FILE=target/*.jar

# Sao chép file JAR vào container
COPY ${JAR_FILE} app.jar

# Expose cổng mà Eureka Server sẽ sử dụng
EXPOSE 8761

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]