FROM openjdk:8
RUN mkdir /app
WORKDIR /app
ADD target/universal/stage /app
CMD /app/bin/upster