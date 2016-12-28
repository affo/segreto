FROM java:8

RUN mkdir /opt/ignite
ENV IGNITE_HOME /opt/ignite
ADD totar.sh /
RUN chmod u+x /totar.sh

RUN curl -o ignite.zip http://mirror.nohup.it/apache//ignite/1.7.0/apache-ignite-fabric-1.7.0-bin.zip
RUN ./totar.sh ignite.zip
RUN tar -zxf ignite.tar.gz --strip-components=1 -C $IGNITE_HOME
RUN ln -s $IGNITE_HOME/bin/ignite.sh /usr/bin/ignite

ENTRYPOINT [ "ignite" ]
