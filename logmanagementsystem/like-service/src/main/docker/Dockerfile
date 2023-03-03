FROM openjdk:8
RUN mkdir -p /usr/local/likeservice
ADD @project.build.finalName@.jar /usr/local/likeservice/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
