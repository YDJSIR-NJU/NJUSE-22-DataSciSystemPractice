FROM openjdk:8
RUN mkdir -p /usr/local/userservice
ADD @project.build.finalName@.jar /usr/local/gateway/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
